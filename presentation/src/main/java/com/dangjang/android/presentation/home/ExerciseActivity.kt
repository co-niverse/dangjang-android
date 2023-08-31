package com.dangjang.android.presentation.home

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.ActivityExerciseBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExerciseActivity : FragmentActivity() {
    private lateinit var binding: ActivityExerciseBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_exercise)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        binding.exerciseEditBtn.setOnClickListener {
            binding.exerciseEditView.visibility = View.VISIBLE
            binding.exerciseTv.visibility = View.GONE
            binding.exerciseEt.visibility = View.VISIBLE

            binding.exerciseEditBtn.visibility = View.GONE
            binding.exerciseOkBtn.visibility = View.VISIBLE
        }

        binding.exerciseOkBtn.setOnClickListener {
            binding.exerciseEditView.visibility = View.GONE
            binding.exerciseTv.visibility = View.VISIBLE
            binding.exerciseEt.visibility = View.GONE

            binding.exerciseEditBtn.visibility = View.VISIBLE
            binding.exerciseOkBtn.visibility = View.GONE
        }

        binding.backIv.setOnClickListener {
            finish()
        }

        binding.exerciseInfoIv.setOnClickListener {
            ExerciseDialogFragment().show(supportFragmentManager, "ExerciseDialogFragment")
        }
    }

}