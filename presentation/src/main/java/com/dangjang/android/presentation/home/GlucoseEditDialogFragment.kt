package com.dangjang.android.presentation.home

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.dangjang.android.domain.constants.ACCESS_TOKEN_KEY
import com.dangjang.android.domain.constants.TOKEN_SPF_KEY
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.FragmentGlucoseEditDialogBinding
import com.navercorp.nid.NaverIdLoginSDK.applicationContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class GlucoseEditDialogFragment : DialogFragment(
) {
    private lateinit var binding: FragmentGlucoseEditDialogBinding
    private val viewModel by activityViewModels<HomeViewModel>()
    private var newType: String = ""
    private var time: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGlucoseEditDialogBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        time = arguments?.getString("time").toString()
        var glucose = arguments?.getString("glucose")

        binding.glucoseAddEt.setText(glucose)
        if (time != null) {
            setGlucoseTimeSpinner(viewModel.glucoseTimeList.indexOf(time))
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.glucoseAddCloseIv.setOnClickListener {
            dismiss()
        }

        binding.glucoseAddSaveBtn.setOnClickListener {
            viewModel.setEditGlucoseCreatedAt(getTodayDate())
            viewModel.setEditGlucoseNewType(newType)
            viewModel.setEditGlucoseType(time)
            viewModel.setEditGlucoseValue(getGlucose())
            getAccessToken()?.let { viewModel.editGlucose(it) }
            dismiss()
        }
    }

    private fun setGlucoseTimeSpinner(index: Int) {
        val glucoseSpinner: Spinner = binding.glucoseSpinner

        val glucoseTimeAdapter = object : ArrayAdapter<String>(applicationContext,
            R.layout.glucose_spinner_dropdown_item, viewModel.glucoseTimeList),
            SpinnerAdapter {
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                // 커스텀한 드롭다운 리스트에 표시할 뷰를 정의합니다.
                val view = super.getDropDownView(position, convertView, parent) as TextView
                view.setTextColor(Color.BLACK) // 드롭다운 리스트의 항목 텍스트 색상 설정
                glucoseSpinner.setSelection(index)
                return view
            }
        }


        glucoseSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                newType = viewModel.glucoseTimeList[position]
            }
        }

        glucoseSpinner.adapter = glucoseTimeAdapter

        glucoseSpinner.setSelection(index)

    }

    private fun getAccessToken(): String? {
        val sharedPreferences = requireContext().getSharedPreferences(TOKEN_SPF_KEY, Context.MODE_PRIVATE)
        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
    }

    private fun getTodayDate(): String {
        val currentTime: Date = Calendar.getInstance().getTime()
        val format = SimpleDateFormat("yyyy-MM-dd")
        return format.format(currentTime)
    }

    private fun getGlucose(): String {
        return binding.glucoseAddEt.text.toString()
    }

}