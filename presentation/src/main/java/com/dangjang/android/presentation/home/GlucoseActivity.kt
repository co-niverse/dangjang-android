package com.dangjang.android.presentation.home

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.dangjang.android.domain.model.GlucoseListVO
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.ActivityGlucoseBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GlucoseActivity : FragmentActivity() {
    private lateinit var binding: ActivityGlucoseBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var glucoseListAdapter: GlucoseListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glucose)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_glucose)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        binding.glucoseAddBtn.setOnClickListener {
            binding.glucoseAddCl.visibility = View.VISIBLE
        }

        binding.glucoseAddCloseIv.setOnClickListener {
            binding.glucoseAddCl.visibility = View.GONE
        }

        binding.glucoseInfoIv.setOnClickListener {
            GlucoseDialogFragment().show(supportFragmentManager, "GlucoseDialogFragment")
        }

        binding.backIv.setOnClickListener {
            finish()
        }

        viewModel.getGlucoseList()
        viewModel.getGlucoseTimeList()

        setGlucoseListAdapter()
        setGlucoseTimeSpinner()
    }

    private fun setGlucoseListAdapter() {
        glucoseListAdapter = GlucoseListAdapter(viewModel.glucoseList)

        glucoseListAdapter.setMyItemClickListener(object :
            GlucoseListAdapter.MyItemClickListener {
            override fun onItemClick(glucoseList: GlucoseListVO) {

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

        glucoseSpinner.adapter = glucoseTimeAdapter

    }


}