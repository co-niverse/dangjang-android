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
    private var glucoseList = arrayListOf<GlucoseListVO>()

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

        glucoseList.add(
                GlucoseListVO(
                    "공복",
                    100,
                    "전반적으로 혈당이 높습니다! 조치가 필요해요",
                    "먹은 과일이 혈당을 높였어요\n운동을 하지 않아 혈당이 높아졌어요"))
        glucoseList.add(
                GlucoseListVO(
                    "아침식전",
                    80,
                    "전반적으로 혈당이 높습니다! 조치가 필요해요",
                    "먹은 과일이 혈당을 높였어요\n" +
                            "운동을 하지 않아 혈당이 높아졌어요"))
        glucoseList.add(
                GlucoseListVO(
                    "취침전",
                    120,
                    "전반적으로 혈당이 높습니다! 조치가 필요해요",
                    "먹은 과일이 혈당을 높였어요\n" +
                            "운동을 하지 않아 혈당이 높아졌어요")
        )
        setGlucoseListAdapter()
        setGlucoseTimeSpinner()
    }

    private fun setGlucoseListAdapter() {
        glucoseListAdapter = GlucoseListAdapter(glucoseList)

        glucoseListAdapter.setMyItemClickListener(object :
            GlucoseListAdapter.MyItemClickListener {
            override fun onItemClick(glucoseList: GlucoseListVO) {
                // TODO : 클릭했을 때 UI 변경 - selector ?
            }
        })

        binding.glucoseRv.adapter = glucoseListAdapter
    }

    private fun setGlucoseTimeSpinner() {
        val glucoseSpinner: Spinner = binding.glucoseSpinner

        val glucoseTimeList = arrayListOf<String>()
        glucoseTimeList.add("공복")
        glucoseTimeList.add("아침 식전")
        glucoseTimeList.add("아침 식후")
        glucoseTimeList.add("점심 식전")
        glucoseTimeList.add("점심 식후")
        glucoseTimeList.add("저녁 식전")
        glucoseTimeList.add("저녁 식후")
        glucoseTimeList.add("취침 전")
        glucoseTimeList.add("기타")

        val glucoseTimeAdapter = object : ArrayAdapter<String>(applicationContext,
            R.layout.glucose_spinner_dropdown_item, glucoseTimeList),
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