package com.dangjang.android.presentation.home

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.FragmentExerciseEditDialogBinding

class ExerciseEditDialogFragment : DialogFragment() {

    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true
    }

    private lateinit var binding: FragmentExerciseEditDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExerciseEditDialogBinding.inflate(inflater, container, false)

        setHourSpinner()
        setMinuteSpinner()

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.exerciseEditCloseIv.setOnClickListener {
            dismiss()
        }
        binding.exerciseEditSaveBtn.setOnClickListener {
            dismiss()
        }
    }

    private fun setHourSpinner() {
        val hourSpinner: Spinner = binding.hourSpinner
        val hourList = viewModel.getHourSpinnerList()

        val hourAdapter = object : ArrayAdapter<String>(
            requireContext(),
            R.layout.custom_spinner_dropdown_item, hourList
        ),
            SpinnerAdapter {
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent) as TextView
                view.setTextColor(Color.BLACK) // 드롭다운 리스트의 항목 텍스트 색상 설정
                return view
            }
        }

        hourSpinner.adapter = hourAdapter

        hourSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
            }
        }

    }

    private fun setMinuteSpinner() {
        val minuteSpinner: Spinner = binding.minuteSpinner
        val minuteList = viewModel.getMinuteSpinnerList()

        val minuteAdapter = object : ArrayAdapter<String>(
            requireContext(),
            R.layout.custom_spinner_dropdown_item, minuteList
        ),
            SpinnerAdapter {
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent) as TextView
                view.setTextColor(Color.BLACK) // 드롭다운 리스트의 항목 텍스트 색상 설정
                return view
            }
        }

        minuteSpinner.adapter = minuteAdapter

        minuteSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
            }
        }

    }
}