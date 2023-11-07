package com.dangjang.android.presentation.home

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.dangjang.android.domain.constants.ACCESS_TOKEN_KEY
import com.dangjang.android.domain.constants.TOKEN_SPF_KEY
import com.dangjang.android.domain.model.GlucoseGuideVO
import com.dangjang.android.domain.model.GlucoseListVO
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.ActivityGlucoseBinding
import com.dangjang.android.presentation.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GlucoseActivity : FragmentActivity() {
    private lateinit var binding: ActivityGlucoseBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var glucoseListAdapter: GlucoseListAdapter
    private var glucoseSpinnerType: String = ""
    private lateinit var glucoseGuideAdapter: GlucoseGuideAdapter
    private var date = ""
    private var startTime: Double = 0.0
    private var endTime: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glucose)

        startTime = System.currentTimeMillis().toDouble()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_glucose)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        binding.vm = viewModel

        binding.lifecycleOwner = this

        date = intent.getStringExtra("date").toString()

        getAccessToken()?.let {
                accessToken -> viewModel.getGlucose(accessToken, date)
        }

        //TODO : 혈당 삭제 시 activity 새로고침 -> 수정 필요
        lifecycleScope.launch {
            viewModel.deleteGlucoseFlow.collectLatest {
                if (it) {
                    finish()
                    overridePendingTransition(0,0)
                    startActivity(intent)
                    overridePendingTransition(0,0)
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.goToLoginActivityFlow.collectLatest {
                if (it) {
                    val intent = Intent(applicationContext, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.getGlucoseFlow.collectLatest {
                if (it.guides.isEmpty())
                    binding.glucoseNoneTv.visibility = View.VISIBLE
                else
                    binding.glucoseNoneTv.visibility = View.GONE
                glucoseGuideAdapter.submitList(viewModel.addBackgroundToTodayGuides(it.todayGuides))
                glucoseListAdapter.submitList(viewModel.addIconToGuides(it.guides))
            }
        }

        binding.glucoseAddSaveBtn.setOnClickListener {
            viewModel.setType(glucoseSpinnerType)
            viewModel.setCreatedAt(date)
            viewModel.setUnit(binding.glucoseAddEt.text.toString())

            if (binding.glucoseAddEt.text.toString() != "") {
                getAccessToken()?.let {
                        accessToken -> viewModel.addHealthMetric(accessToken)
                }
            }
            binding.glucoseAddCl.visibility = View.GONE
            binding.glucoseAddEt.text.clear()
        }

        binding.glucoseAddBtn.setOnClickListener {
            binding.glucoseAddCl.visibility = View.VISIBLE
        }

        binding.glucoseAddCloseIv.setOnClickListener {
            binding.glucoseAddCl.visibility = View.GONE
            binding.glucoseAddEt.text.clear()
        }

        binding.glucoseInfoIv.setOnClickListener {
            GlucoseDialogFragment().show(supportFragmentManager, "GlucoseDialogFragment")
        }

        binding.backIv.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("date",date)
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        viewModel.getGlucoseTimeList()

        setGlucoseListAdapter()
        setGlucoseTimeSpinner()
        setGlucoseGuideListAdapter()
    }

    private fun setGlucoseListAdapter() {
        glucoseListAdapter = GlucoseListAdapter(viewModel)

        glucoseListAdapter.setMyItemClickListener(object :
            GlucoseListAdapter.MyItemClickListener {
            override fun onEditBtnClick(glucoseList: GlucoseListVO) {
                var glucoseEditDialogFragment = GlucoseEditDialogFragment()
                var bundle = Bundle()
                bundle.putString("time", glucoseList.time)
                bundle.putString("glucose", glucoseList.glucose)
                bundle.putString("date", date)
                glucoseEditDialogFragment.arguments = bundle

                glucoseEditDialogFragment.show(supportFragmentManager, "GlucoseEditDialogFragment")
            }

            override fun onDeleteBtnClick(glucoseList: GlucoseListVO) {
                var glucoseDeleteDialogFragment = GlucoseDeleteDialogFragment()
                var bundle = Bundle()
                bundle.putString("time", glucoseList.time)
                bundle.putString("date", date)
                glucoseDeleteDialogFragment.arguments = bundle

                glucoseDeleteDialogFragment.show(supportFragmentManager, "GlucoseDeleteDialogFragment")
            }
        })
        binding.glucoseRv.adapter = glucoseListAdapter
    }

    private fun setGlucoseTimeSpinner() {
        val glucoseSpinner: Spinner = binding.glucoseSpinner

        val glucoseTimeAdapter = object : ArrayAdapter<String>(applicationContext,
            R.layout.glucose_spinner_dropdown_item, viewModel.glucoseTimeList),
            SpinnerAdapter {
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                // 커스텀한 드롭다운 리스트에 표시할 뷰를 정의합니다.
                val view = super.getDropDownView(position, convertView, parent) as TextView
                view.setTextColor(Color.BLACK) // 드롭다운 리스트의 항목 텍스트 색상 설정
                return view
            }
        }

        glucoseSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                glucoseSpinnerType = viewModel.glucoseTimeList[position]
            }
        }

        glucoseSpinner.adapter = glucoseTimeAdapter

    }

    override fun onDestroy() {
        super.onDestroy()
        endTime = System.currentTimeMillis().toDouble()
        viewModel.shotGlucoseStayTimeLogging(endTime- startTime)
        viewModel.shotGlucoseExposureLogging(endTime - startTime)
    }

    private fun getAccessToken(): String? {
        val sharedPreferences = getSharedPreferences(TOKEN_SPF_KEY, Context.MODE_PRIVATE)

        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
    }

    private fun setGlucoseGuideListAdapter() {
        glucoseGuideAdapter = GlucoseGuideAdapter(viewModel)
        glucoseGuideAdapter.setMyItemClickListener(
            object : GlucoseGuideAdapter.MyItemClickListener {
                override fun onItemClick(glucoseGuideList: GlucoseGuideVO) {
                }
            }
        )
        binding.glucoseGuideRv.adapter = glucoseGuideAdapter
    }
}