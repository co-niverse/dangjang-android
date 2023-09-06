package com.dangjang.android.presentation.home

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.FragmentGlucoseEditDialogBinding
import com.navercorp.nid.NaverIdLoginSDK.applicationContext

class GlucoseEditDialogFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true
    }

    private lateinit var binding: FragmentGlucoseEditDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGlucoseEditDialogBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        setGlucoseTimeSpinner()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.glucoseAddCloseIv.setOnClickListener {
            dismiss()
        }

        binding.glucoseAddSaveBtn.setOnClickListener {
            dismiss()
        }
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

        val glucoseTimeAdapter = object : ArrayAdapter<String>(
            applicationContext,
            R.layout.glucose_spinner_dropdown_item, glucoseTimeList
        ),
            SpinnerAdapter {
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                // 커스텀한 드롭다운 리스트에 표시할 뷰를 정의합니다.
                val view = super.getDropDownView(position, convertView, parent) as TextView
                view.setTextColor(Color.BLACK) // 드롭다운 리스트의 항목 텍스트 색상 설정
                return view
            }
        }

        glucoseSpinner.adapter = glucoseTimeAdapter

    }

}