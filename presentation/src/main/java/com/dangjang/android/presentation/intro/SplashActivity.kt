package com.dangjang.android.presentation.intro

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dangjang.android.domain.constants.ACCESS_TOKEN_KEY
import com.dangjang.android.domain.constants.AUTO_LOGIN_EDITOR_KEY
import com.dangjang.android.domain.constants.AUTO_LOGIN_SPF_KEY
import com.dangjang.android.domain.constants.HEALTH_CONNECT_INSTALLED
import com.dangjang.android.domain.constants.HEALTH_CONNECT_NOT_INSTALLED
import com.dangjang.android.domain.constants.HEALTH_CONNECT_TOKEN_KEY
import com.dangjang.android.domain.constants.KAKAO
import com.dangjang.android.domain.constants.TOKEN_SPF_KEY
import com.dangjang.android.domain.request.HealthConnectRequest
import com.dangjang.android.presentation.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private val viewModel by viewModels<SplashViewModel>()
    private var hcExerciseGlucoseList = mutableListOf<HealthConnectRequest>()
    private var hcWeightStepList = mutableListOf<HealthConnectRequest>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler().postDelayed(Runnable {
            val i = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(i)
            finish()
        }, 5000)

        val sp: SharedPreferences = getSharedPreferences(AUTO_LOGIN_SPF_KEY, MODE_PRIVATE)
        val provider = sp.getString(AUTO_LOGIN_EDITOR_KEY, "null")
        Log.e("sp", provider.toString())

        viewModel.getIntroData()

        viewModel.checkAvailability()

        val healthConnect = sp.getString(HEALTH_CONNECT_TOKEN_KEY, "null")

        if (healthConnect == "true") {
            if (viewModel.healthConnectFlow.value.isAvaiable == HEALTH_CONNECT_INSTALLED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    viewModel.getAllHealthConnectData()

                    lifecycleScope.launch {
                        viewModel.hcExerciseList.zip(viewModel.hcGlucoseList) { exerciseList, glucoseList ->
                            Log.e("exerciseList", exerciseList.toString())
                            Log.e("glucoseList", glucoseList.toString())
                        }.collect {
                            if (viewModel.hcStepList.value.isNotEmpty() && viewModel.hcWeightList.value.isNotEmpty()) {
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
                            if (viewModel.hcExerciseGlucoseList.value.isNotEmpty() && viewModel.hcWeightStepList.value.isNotEmpty()) {
                                getAccessToken()?.let { it1 -> viewModel.postHealthConnectData(it1) }
                            }
                        }
                    }
                }
            }

        } else if (healthConnect == "false") {
            if (viewModel.healthConnectFlow.value.isAvaiable == HEALTH_CONNECT_NOT_INSTALLED) {
                //TODO : 헬스커넥트 팝업 띄우기
            }
        }

//        if (provider == KAKAO) {
//            startActivity(Intent(this, LoginActivity::class.java))
//            finish()
//        } else if (provider == NAVER) {
//            startActivity(Intent(this, MainActivity::class.java))
//            finish()
//        } else {
//            startActivity(Intent(this, LoginActivity::class.java))
//            finish()
//        }
    }


    private fun getAccessToken(): String? {
        val sharedPreferences = getSharedPreferences(TOKEN_SPF_KEY, Context.MODE_PRIVATE)
        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
    }
}