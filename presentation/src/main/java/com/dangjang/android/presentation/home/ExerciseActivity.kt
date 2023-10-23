package com.dangjang.android.presentation.home

import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.dangjang.android.domain.constants.ACCESS_TOKEN_KEY
import com.dangjang.android.domain.constants.TOKEN_SPF_KEY
import com.dangjang.android.domain.model.ExerciseListVO
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.ActivityExerciseBinding
import com.dangjang.android.presentation.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ExerciseActivity : FragmentActivity() {
    private lateinit var binding: ActivityExerciseBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var exerciseListAdapter: ExerciseListAdapter
    private var originStep: Int = 0
    private var date = ""
    private var openBtnFlag = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_exercise)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        binding.vm = viewModel

        binding.lifecycleOwner = this

        date = intent.getStringExtra("date").toString()

        getAccessToken()?.let { viewModel.getExercise(it, date) }

        lifecycleScope.launchWhenStarted {
            viewModel.goToLoginActivityFlow.collectLatest {
                if (it) {
                    val intent = Intent(applicationContext, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

        binding.exerciseOpenBtn.setOnClickListener {
            if (openBtnFlag) {
                binding.exerciseRv.visibility = View.VISIBLE
                binding.exerciseOpenBtn.text = "숨기기"
            } else {
                binding.exerciseRv.visibility = View.GONE
                binding.exerciseOpenBtn.text = "추가하기"
            }
            openBtnFlag = !openBtnFlag
        }

        lifecycleScope.launchWhenStarted {
            viewModel.getExerciseFlow.collectLatest {
                if (it.stepsCount == 0) {
                    binding.exerciseNoneTv.visibility = View.VISIBLE
                } else {
                    binding.exerciseNoneTv.visibility = View.GONE
                }
                exerciseListAdapter.submitList(viewModel.changeExerciseCaloriesToExerciseList(it.exerciseCalories))

                var totalCalorie = 0

                it.exerciseCalories.forEach {
                    when (it.type) {
                        "WALK" -> {
                            binding.exerciseStepKcalTv.visibility = View.VISIBLE
                            binding.exerciseStepKcalTv.text = "걷기 ${it.calorie}kcal"
                        }
                        "RUN" -> {
                            binding.exerciseRunningKcalTv.visibility = View.VISIBLE
                            binding.exerciseRunningKcalTv.text = "달리기 ${it.calorie}kcal"
                        }
                        "HIKING" -> {
                            binding.exerciseHikingKcalTv.visibility = View.VISIBLE
                            binding.exerciseHikingKcalTv.text = "등산 ${it.calorie}kcal"
                        }
                        "BIKE" -> {
                            binding.exerciseBikeKcalTv.visibility = View.VISIBLE
                            binding.exerciseBikeKcalTv.text = "자전거 ${it.calorie}kcal"
                        }
                        "SWIM" -> {
                            binding.exerciseSwimKcalTv.visibility = View.VISIBLE
                            binding.exerciseSwimKcalTv.text = "수영 ${it.calorie}kcal"
                        }
                        "HEALTH" -> {
                            binding.exerciseHealthKcalTv.visibility = View.VISIBLE
                            binding.exerciseHealthKcalTv.text = "헬스 ${it.calorie}kcal"
                        }
                    }
                    totalCalorie += it.calorie
                }

                binding.exerciseFeedbackKcalTv.text = "총 ${totalCalorie}kcal"
            }
        }

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

            originStep = binding.stepTv.text.toString().toInt()

            if (originStep == 0) {
                viewModel.setExerciseTypeAndCreatedAt("걸음수", date)
                viewModel.setExerciseUnit(binding.stepEt.text.toString())
                getAccessToken()?.let { viewModel.addExercise(it) }
            } else {
                viewModel.setEditExerciseTypeAndCreatedAt("걸음수", date)
                viewModel.setEditExerciseUnit(binding.stepEt.text.toString())
                getAccessToken()?.let { viewModel.editExercise(it) }
            }

        }

        binding.backIv.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("date",date)
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        binding.exerciseInfoIv.setOnClickListener {
            ExerciseDialogFragment().show(supportFragmentManager, "ExerciseDialogFragment")
        }

        setExerciseListAdapter()

    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.shotExerciseExposureLogging()
    }

    private fun setExerciseListAdapter() {
        exerciseListAdapter = ExerciseListAdapter(viewModel)

        exerciseListAdapter.setMyItemClickListener(object :
            ExerciseListAdapter.MyItemClickListener {

            override fun onItemClick(exerciseList: ExerciseListVO) {
                var exerciseEditDialogFragment = ExerciseEditDialogFragment()
                var bundle = Bundle()
                bundle.putString("type", exerciseList.exerciseName)
                bundle.putString("hour", exerciseList.exerciseHour)
                bundle.putString("minute", exerciseList.exerciseMinute)
                bundle.putString("date",date)
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