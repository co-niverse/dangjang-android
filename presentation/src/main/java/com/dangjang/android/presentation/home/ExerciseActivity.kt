package com.dangjang.android.presentation.home

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.dangjang.android.domain.constants.ACCESS_TOKEN_KEY
import com.dangjang.android.domain.constants.TOKEN_SPF_KEY
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
    private var originStep: Int = 0

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

            if (originStep == 0) {
                viewModel.setExerciseTypeAndCreatedAt("걸음수")
                viewModel.setExerciseUnit(binding.stepEt.text.toString())
                getAccessToken()?.let { it1 -> viewModel.addExercise(it1) }
            } else {
                // TODO : 걸음수 수정
            }

        }

        binding.backIv.setOnClickListener {
            finish()
        }

        binding.exerciseInfoIv.setOnClickListener {
            ExerciseDialogFragment().show(supportFragmentManager, "ExerciseDialogFragment")
        }

        exerciseList.add(ExerciseListVO("걷기","0","0"))
        exerciseList.add(ExerciseListVO("달리기","0","0"))
        exerciseList.add(ExerciseListVO("하이킹","0","0"))
        exerciseList.add(ExerciseListVO("자전거","0","0"))
        exerciseList.add(ExerciseListVO("수영","0","0"))
        exerciseList.add(ExerciseListVO("헬스","0","0"))

        setExerciseListAdapter()

    }

    private fun setExerciseListAdapter() {
        exerciseListAdapter = ExerciseListAdapter(exerciseList)

        exerciseListAdapter.setMyItemClickListener(object :
            ExerciseListAdapter.MyItemClickListener {

            override fun onItemClick(exerciseList: ExerciseListVO) {
                var exerciseEditDialogFragment = ExerciseEditDialogFragment()
                var bundle = Bundle()
                bundle.putString("type", exerciseList.exerciseName)
                bundle.putString("hour", exerciseList.exerciseHour)
                bundle.putString("minute", exerciseList.exerciseMinute)
                exerciseEditDialogFragment.arguments = bundle
                exerciseEditDialogFragment.show(supportFragmentManager, "ExerciseEditDialogFragment")
            }
        })

        binding.exerciseRv.adapter = exerciseListAdapter
    }

    private fun getAccessToken(): String? {
        val sharedPreferences = getSharedPreferences(TOKEN_SPF_KEY, Context.MODE_PRIVATE)

        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
    }
}