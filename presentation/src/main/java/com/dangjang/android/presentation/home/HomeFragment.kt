package com.dangjang.android.presentation.home

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.dangjang.android.common_ui.BaseFragment
import com.dangjang.android.domain.constants.AUTO_LOGIN_SPF_KEY
import com.dangjang.android.domain.constants.HEALTH_CONNECT_TOKEN_KEY
import com.dangjang.android.domain.constants.ACCESS_TOKEN_KEY
import com.dangjang.android.domain.constants.TOKEN_SPF_KEY
import com.dangjang.android.domain.model.GlucoseGuideVO
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var glucoseGuideAdapter: GlucoseGuideAdapter
    private lateinit var date: String
    private var intentDate = ""

    override fun initView() {
        bind {
            vm = viewModel
        }
    }
    override fun onStart() {
        super.onStart()

        binding.vm = viewModel
        binding.lifecycleOwner = this

        date = if (intentDate == "") {
            viewModel.getTodayDate()
        } else {
            intentDate
        }

        getAccessToken()?.let { viewModel.getHome(it, date) }

        binding.weightSeekbar.setOnTouchListener({ v, event -> true })

        binding.calendarIv.setOnClickListener {
            Locale.setDefault(Locale.KOREA)

            val cal = Calendar.getInstance()
            val data = DatePickerDialog.OnDateSetListener { view, year, month, day ->
                getAccessToken()?.let { viewModel.getHome(it, viewModel.getDatePickerDate(year, month, day)) }
                date = viewModel.getDatePickerDate(year, month, day)
            }
            val datePickerDialog = DatePickerDialog(requireContext(),data,cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.show()
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
            Intent(activity, GlucoseActivity::class.java).apply {
                putExtra("date",date)
                startActivityForResult(this, 101)
            }
        }

        binding.weightCl.setOnClickListener {
            Intent(activity, WeightActivity::class.java).apply {
                putExtra("date",date)
                startActivityForResult(this, 101)
            }
        }

        binding.exerciseCl.setOnClickListener {
            Intent(activity, ExerciseActivity::class.java).apply {
                putExtra("date",date)
                startActivityForResult(this, 101)
            }
        }

        val sp: SharedPreferences = requireContext().getSharedPreferences(AUTO_LOGIN_SPF_KEY, AppCompatActivity.MODE_PRIVATE)
        val healthConnect = sp.getString(HEALTH_CONNECT_TOKEN_KEY, "null")

        if (healthConnect == "false") {
            binding.autoInputBtn.visibility = View.VISIBLE
            binding.autoInputBtn.setOnClickListener {
                HealthConnectBottomSheetFragment().show(parentFragmentManager, "HealthConnectBottomSheetFragment")
            }
        }

        setGlucoseGuideListAdapter()

        binding.caloryCl.setOnClickListener {
            viewModel.shotCalorieClickLogging()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val result = data?.getStringExtra("date")
            intentDate = result.toString()
        }
    }

    private fun setGlucoseGuideListAdapter() {
        glucoseGuideAdapter = GlucoseGuideAdapter(viewModel)

        glucoseGuideAdapter.setMyItemClickListener(object :
            GlucoseGuideAdapter.MyItemClickListener {
            override fun onItemClick(glucoseGuideList: GlucoseGuideVO) {
                Intent(activity, GlucoseActivity::class.java).apply {
                    startActivity(this)
                }
            }

        })

        binding.homeGlucoseRv.adapter = glucoseGuideAdapter
    }

    private fun getAccessToken(): String? {
        val sharedPreferences = requireContext().getSharedPreferences(TOKEN_SPF_KEY, Context.MODE_PRIVATE)

        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
    }
}