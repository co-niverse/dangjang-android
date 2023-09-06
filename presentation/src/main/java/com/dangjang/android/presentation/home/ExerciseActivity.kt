package com.dangjang.android.presentation.home

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.dangjang.android.domain.model.ExerciseListVO
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.ActivityExerciseBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExerciseActivity : FragmentActivity() {
    private lateinit var binding: ActivityExerciseBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var exerciseListAdapter: ExerciseListAdapter
    private var exerciseList = arrayListOf<ExerciseListVO>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_exercise)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        binding.stepEditBtn.setOnClickListener {
            binding.stepEditView.visibility = View.VISIBLE
            binding.stepTv.visibility = View.GONE
            binding.stepEt.visibility = View.VISIBLE

            binding.stepEditBtn.visibility = View.GONE
            binding.stepOkBtn.visibility = View.VISIBLE
        }

        binding.stepOkBtn.setOnClickListener {
            binding.stepEditView.visibility = View.GONE
            binding.stepTv.visibility = View.VISIBLE
            binding.stepEt.visibility = View.GONE

            binding.stepEditBtn.visibility = View.VISIBLE
            binding.stepOkBtn.visibility = View.GONE
        }

        binding.backIv.setOnClickListener {
            finish()
        }

        binding.exerciseInfoIv.setOnClickListener {
            ExerciseDialogFragment().show(supportFragmentManager, "ExerciseDialogFragment")
        }

        exerciseList.add(ExerciseListVO("걷기","1","30"))
        exerciseList.add(ExerciseListVO("달리기","2","10"))
        exerciseList.add(ExerciseListVO("등산","1","10"))
        exerciseList.add(ExerciseListVO("자전거","0","30"))
        exerciseList.add(ExerciseListVO("수영","1","15"))
        exerciseList.add(ExerciseListVO("헬스","0","20"))

        setExerciseListAdapter()

    }

    private fun setExerciseListAdapter() {
        exerciseListAdapter = ExerciseListAdapter(exerciseList)

        exerciseListAdapter.setMyItemClickListener(object :
            ExerciseListAdapter.MyItemClickListener {

            override fun onItemClick(exerciseList: ExerciseListVO) {

            }
        })

        binding.exerciseRv.adapter = exerciseListAdapter
    }
}