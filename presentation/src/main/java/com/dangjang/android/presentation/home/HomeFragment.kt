package com.dangjang.android.presentation.home

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.dangjang.android.common_ui.BaseFragment
import com.dangjang.android.domain.constants.AUTO_LOGIN_SPF_KEY
import com.dangjang.android.domain.constants.HEALTH_CONNECT_TOKEN_KEY
import com.dangjang.android.domain.constants.ACCESS_TOKEN_KEY
import com.dangjang.android.domain.constants.TOKEN_SPF_KEY
import com.dangjang.android.domain.constants.VERSION_SPF_KEY
import com.dangjang.android.domain.constants.VERSION_TOKEN_KEY
import com.dangjang.android.domain.model.GlucoseGuideVO
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.FragmentHomeBinding
import com.dangjang.android.presentation.intro.UpdateBottomSheetFragment
import com.dangjang.android.presentation.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var glucoseGuideAdapter: GlucoseGuideAdapter
    private lateinit var date: String
    private var intentDate = ""
    private var startTime: Double = 0.0
    private var endTime: Double = 0.0

    override fun initView() {
        bind {
            vm = viewModel
        }
    }
    override fun onStart() {
        super.onStart()

        startTime = System.currentTimeMillis().toDouble()

        binding.vm = viewModel
        binding.lifecycleOwner = this

        date = viewModel.getDateFlow.value

        getAccessToken()?.let { viewModel.getHome(it, viewModel.getDateFlow.value) }

//        viewModel.getIntroData()
//        lifecycleScope.launch {
//            viewModel.introDataFlow.collectLatest {
//                if (it.latestVersion != "") {
//                    if (it.latestVersion != getVersionName()) {
//                        UpdateBottomSheetFragment().show(parentFragmentManager, "UpdateBottomSheetFragment")
//                    }
//                }
//            }
//        }

        binding.weightSeekbar.isEnabled = false

        binding.calendarIv.setOnClickListener {
            viewModel.shotCalendarClickLogging()

            Locale.setDefault(Locale.KOREA)

            val cal = Calendar.getInstance()
            val data = DatePickerDialog.OnDateSetListener { view, year, month, day ->
                getAccessToken()?.let { viewModel.getHome(it, viewModel.getDatePickerDate(year, month, day)) }
                date = viewModel.getDatePickerDate(year, month, day)
                viewModel.setDate(viewModel.getDatePickerDate(year, month, day))
            }
            val datePickerDialog = DatePickerDialog(requireContext(),data,cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.show()
        }

        lifecycleScope.launchWhenStarted {
            viewModel.goToLoginActivityFlow.collectLatest {
                if (it) {
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.getHomeFlow.collectLatest {
                if (it.bloodSugars.isNullOrEmpty()) {
                    binding.glucoseGuideNoneTv.visibility = View.VISIBLE
                }
                else {
                    binding.glucoseGuideNoneTv.visibility = View.GONE
                }
                val bloodSugarsList = viewModel.addBackgroundToTodayGuides(it.bloodSugars)
                glucoseGuideAdapter.submitList(bloodSugarsList)

                binding.weightSeekbar.progress = viewModel.calculateSeekbarProgress(it.weight.bmi)

                if (it.weight.unit == "") {
                    binding.weightNoneTv.visibility = View.VISIBLE
                } else {
                    binding.weightNoneTv.visibility = View.GONE
                }

                if (it.notification) {
                    binding.bellExistView.visibility = View.VISIBLE
                } else {
                    binding.bellExistView.visibility = View.GONE
                }

            }
        }

        binding.glucoseCl.setOnClickListener {
            viewModel.shotGlucoseClickLogging()
            Intent(activity, GlucoseActivity::class.java).apply {
                putExtra("date",date)
                startActivityForResult(this, 101)
            }
        }

        binding.weightCl.setOnClickListener {
            viewModel.shotWeightClickLogging()
            Intent(activity, WeightActivity::class.java).apply {
                putExtra("date",date)
                startActivityForResult(this, 101)
            }
        }

        binding.exerciseCl.setOnClickListener {
            viewModel.shotExerciseClickLogging()
            Intent(activity, ExerciseActivity::class.java).apply {
                putExtra("date",date)
                startActivityForResult(this, 101)
            }
        }

        binding.bellIv.setOnClickListener {
            Intent(activity, AlarmActivity::class.java).apply {
                startActivity(this)
            }
        }

        val sp: SharedPreferences = requireContext().getSharedPreferences(AUTO_LOGIN_SPF_KEY, AppCompatActivity.MODE_PRIVATE)
        val healthConnect = sp.getString(HEALTH_CONNECT_TOKEN_KEY, "null")

        //TODO : 헬스커넥트 연동한 사람한테는 버튼 숨기기
//        if (healthConnect == "false") {
            binding.autoInputBtn.visibility = View.VISIBLE
            binding.autoInputBtn.setOnClickListener {
                HealthConnectBottomSheetFragment().show(parentFragmentManager, "HealthConnectBottomSheetFragment")
            }
 //       }

        setGlucoseGuideListAdapter()

        binding.caloryCl.setOnClickListener {
            viewModel.shotCalorieClickLogging()
            Toast.makeText(context, "서비스 준비중입니다.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        endTime = System.currentTimeMillis().toDouble()
        viewModel.shotHomeExposureLogging(endTime - startTime)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val result = data?.getStringExtra("date")
            intentDate = result.toString()
            if (intentDate == "") {
                getAccessToken()?.let { viewModel.getHome(it, viewModel.getTodayDate()) }
                viewModel.setDate(viewModel.getTodayDate())
            } else {
                getAccessToken()?.let { viewModel.getHome(it, intentDate) }
                viewModel.setDate(intentDate)
            }
        }
    }

    private fun setGlucoseGuideListAdapter() {
        glucoseGuideAdapter = GlucoseGuideAdapter(viewModel)

        glucoseGuideAdapter.setMyItemClickListener(object :
            GlucoseGuideAdapter.MyItemClickListener {
            override fun onItemClick(glucoseGuideList: GlucoseGuideVO) {
                viewModel.shotGlucoseClickLogging()
                Intent(activity, GlucoseActivity::class.java).apply {
                    putExtra("date",date)
                    startActivityForResult(this, 101)
                }
            }

        })

        binding.homeGlucoseRv.adapter = glucoseGuideAdapter
    }

    private fun getAccessToken(): String? {
        val sharedPreferences = requireContext().getSharedPreferences(TOKEN_SPF_KEY, Context.MODE_PRIVATE)

        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
    }

    private fun getVersionName(): String? {
        val sharedPreferences = requireContext().getSharedPreferences(VERSION_SPF_KEY, Context.MODE_PRIVATE)
        return sharedPreferences.getString(VERSION_TOKEN_KEY, null)
    }
}