package com.dangjang.android.presentation.home

import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.ActivitySleepBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class SleepActivity : FragmentActivity() {
    private lateinit var binding: ActivitySleepBinding
    private lateinit var viewModel: SleepViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sleep)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sleep)
        viewModel = ViewModelProvider(this).get(SleepViewModel::class.java)

        binding.sleepEditBtn.setOnClickListener {
            //binding.nightEditView.visibility = View.VISIBLE
            binding.nightTv.visibility = View.GONE
            binding.nightEt.visibility = View.VISIBLE

            binding.morningEditView.visibility = View.VISIBLE
            binding.morningTv.visibility = View.GONE
            binding.morningEt.visibility = View.VISIBLE

            binding.sleepEditBtn.visibility = View.GONE
            binding.sleepOkBtn.visibility = View.VISIBLE
        }

        binding.sleepOkBtn.setOnClickListener {
            binding.morningEditView.visibility = View.GONE
            binding.morningTv.visibility = View.VISIBLE
            binding.morningEt.visibility = View.GONE

            //binding.nightEditView.visibility = View.GONE
            binding.nightTv.visibility = View.VISIBLE
            binding.nightEt.visibility = View.GONE

            binding.sleepEditBtn.visibility = View.VISIBLE
            binding.sleepOkBtn.visibility = View.GONE
        }

        binding.backIv.setOnClickListener {
            finish()
        }

        binding.nightTv.setOnClickListener {
            getTime(binding.nightTv, this)
        }

        binding.morningTv.setOnClickListener {
            getTime(binding.morningTv, this)
        }

    }

    fun getTime(textView: TextView, context: Context){
        val cal = Calendar.getInstance()
        val timeSetListener = OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            textView.text = String.format("%02d:%02d", hour, minute)
        }
//        TimePickerDialog(context, android.R.style.Theme_Material_Light_Dialog, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        TimePickerDialog(context, android.R.style.Widget_Material_Light_DropDownItem_Spinner, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }

}