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
import com.dangjang.android.presentation.databinding.FragmentExerciseEditDialogBinding

class ExerciseEditDialogFragment : DialogFragment() {

    private val viewModel by activityViewModels<HomeViewModel>()
    private var exerciseName: String = ""
    private var exerciseHour: String = ""
    private var exerciseMinute: String = ""

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

        exerciseName = arguments?.getString("type").toString()
        exerciseHour = arguments?.getString("hour").toString()
        exerciseMinute = arguments?.getString("minute").toString()

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
            if (exerciseHour == "0" && exerciseMinute == "0") {
                viewModel.setExerciseTypeAndCreatedAt(exerciseName)
                viewModel.setExerciseUnit(getExerciseTime(exerciseHour, exerciseMinute))
                getAccessToken()?.let { viewModel.addExercise(it) }
                dismiss()
            } else {
                viewModel.setEditExerciseTypeAndCreatedAt(exerciseName)
                viewModel.setEditExerciseUnit(getExerciseTime(exerciseHour, exerciseMinute))
                getAccessToken()?.let { viewModel.editExercise(it) }
                dismiss()
            }
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
        hourSpinner.setSelection(hourList.indexOf(exerciseHour))

        hourSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                exerciseHour = hourList[position]
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
        minuteSpinner.setSelection(minuteList.indexOf(exerciseMinute))

        minuteSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                exerciseMinute = minuteList[position]
            }
        }

    }

    private fun getAccessToken(): String? {
        val sharedPreferences = requireContext().getSharedPreferences(TOKEN_SPF_KEY, Context.MODE_PRIVATE)

        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
    }

    private fun getExerciseTime(hour: String, minute: String): String {
        var exerciseTime = hour.toInt() * 60 + minute.toInt()
        return exerciseTime.toString()
    }
}