package com.dangjang.android.presentation.intro

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.dangjang.android.domain.constants.ACCESS_TOKEN_KEY
import com.dangjang.android.domain.constants.HEALTH_CONNECT_INSTALLED
import com.dangjang.android.domain.constants.TOKEN_SPF_KEY
import com.dangjang.android.domain.request.HealthConnectRequest
import com.dangjang.android.presentation.MainActivity
import com.dangjang.android.presentation.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HealthConnectActivity : FragmentActivity() {
    private val viewModel by viewModels<HealthConnectViewModel>()
    private var hcExerciseGlucoseList = mutableListOf<HealthConnectRequest>()
    private var hcWeightStepList = mutableListOf<HealthConnectRequest>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_healthconnect)

        viewModel.checkAvailability()
        viewModel.getHealthMetricLastDate(getAccessToken() ?: "")
//        val healthConnectAvailability = viewModel.getHealtConnectSpf()
        viewModel.checkHealthConnectInterlock()

//        if (healthConnectAvailability == "true") {
            if (viewModel.healthConnectFlow.value.isAvaiable == HEALTH_CONNECT_INSTALLED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    lifecycleScope.launch {
                        viewModel.healthMetricLastDate.collectLatest {
                            if (it.date != "") {
                                Log.e("healthMetricLastDate", it.date)
                                viewModel.getAllHealthConnectData()
                            }
                        }
                    }

                    //TODO : view model로 이동
                    lifecycleScope.launch {
                        viewModel.hcExerciseList.zip(viewModel.hcGlucoseList) { exerciseList, glucoseList ->
                            Log.e("exerciseList", exerciseList.toString())
                            Log.e("glucoseList", glucoseList.toString())
                        }.collect {
                            Log.e(
                                "zip1Test",
                                viewModel.hcExerciseList.value.toString() + viewModel.hcGlucoseList.value.toString()
                            )
                            if (viewModel.hcExerciseList.value.isNotEmpty() && viewModel.hcGlucoseList.value.isNotEmpty()) {
                                hcExerciseGlucoseList.addAll(viewModel.hcExerciseList.value)
                                hcExerciseGlucoseList.addAll(viewModel.hcGlucoseList.value)
                                viewModel.setHcExerciseGlucoseList(hcExerciseGlucoseList)
                                Log.e(
                                    "zip1",
                                    viewModel.hcExerciseList.value.toString() + viewModel.hcGlucoseList.value.toString()
                                )
                            }
                        }
                    }

                    lifecycleScope.launch {
                        viewModel.hcStepList.zip(viewModel.hcWeightList) { stepList, weightList ->
                            Log.e("stepList", stepList.toString())
                            Log.e("weightList", weightList.toString())
                        }.collect {
                            Log.e(
                                "zip2Test",
                                viewModel.hcExerciseList.value.toString() + viewModel.hcGlucoseList.value.toString()
                            )
                            if (viewModel.hcStepList.value.isNotEmpty() && viewModel.hcWeightList.value.isNotEmpty()) {
                                hcWeightStepList.addAll(viewModel.hcStepList.value)
                                hcWeightStepList.addAll(viewModel.hcWeightList.value)
                                viewModel.setHcWeightStepList(hcWeightStepList)
                                Log.e(
                                    "zip2",
                                    viewModel.hcStepList.value.toString() + viewModel.hcWeightList.value.toString()
                                )
                            }
                        }
                    }

                    lifecycleScope.launch {
                        viewModel.hcExerciseGlucoseList.zip(viewModel.hcWeightStepList) { exerciseGlucoseList, weightStepList ->
                            Log.e("exerciseGlucoseList", exerciseGlucoseList.toString())
                            Log.e("weightStepList", weightStepList.toString())
                        }.collect {
                            getAccessToken()?.let { it1 ->
                                Log.e(
                                    "postTest",
                                    viewModel.hcExerciseList.value.toString() + viewModel.hcGlucoseList.value.toString()
                                )
                            if (viewModel.hcExerciseGlucoseList.value.isNotEmpty() || viewModel.hcWeightStepList.value.isNotEmpty()) {
                                getAccessToken()?.let { it1 -> viewModel.postHealthConnectData(it1) }
                            }
                            }
                        }
                    }

                    lifecycleScope.launch {
                        viewModel.postHealthConnectFlow.collect {
                            if (it) {
                                goToMainActivity()
                            }
                        }
                    }
                }
            }

//        } else if (healthConnectAvailability == "false") {
        else {
            goToMainActivity()
        }

        lifecycleScope.launch {
            viewModel.patchHealthConnectInterlockFlow.collectLatest {
                Log.e("patchHealthConnectInterlockFlow", it)
                if (it == "false") {
                    goToMainActivity()
                }
            }
        }

    }

    private fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun getAccessToken(): String? {
        val sharedPreferences = getSharedPreferences(TOKEN_SPF_KEY, Context.MODE_PRIVATE)
        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
    }

}