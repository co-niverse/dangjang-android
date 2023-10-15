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
import com.dangjang.android.domain.constants.HEALTH_CONNECT_NOT_INSTALLED
import com.dangjang.android.domain.constants.KAKAO
import com.dangjang.android.domain.constants.NAVER
import com.dangjang.android.domain.constants.TOKEN_SPF_KEY
import com.dangjang.android.domain.constants.VERSION_INTRO_TOKEN_KEY
import com.dangjang.android.domain.constants.VERSION_SPF_KEY
import com.dangjang.android.domain.constants.VERSION_TOKEN_KEY
import com.dangjang.android.domain.request.HealthConnectRequest
import com.dangjang.android.presentation.MainActivity
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.login.LoginActivity
import com.dangjang.android.presentation.signup.SignupActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashActivity : FragmentActivity() {
    private val viewModel by viewModels<SplashViewModel>()
    private var hcExerciseGlucoseList = mutableListOf<HealthConnectRequest>()
    private var hcWeightStepList = mutableListOf<HealthConnectRequest>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        viewModel.getIntroData()

        viewModel.checkAvailability()

        lifecycleScope.launch {
            viewModel.introDataFlow.collectLatest {
                setIntroVersionName(it.latestVersion)
            }
        }

        goToMainOrLoginActivity()

//        val healthConnectAvailability = viewModel.getHealtConnectSpf()
//        if (healthConnectAvailability == "true") {
//            if (viewModel.healthConnectFlow.value.isAvaiable == HEALTH_CONNECT_INSTALLED) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    viewModel.getAllHealthConnectData()
//
//                    //TODO : view model로 이동
//                    lifecycleScope.launch {
//                        viewModel.hcExerciseList.zip(viewModel.hcGlucoseList) { exerciseList, glucoseList ->
//                            Log.e("exerciseList", exerciseList.toString())
//                            Log.e("glucoseList", glucoseList.toString())
//                        }.collect {
//                            if (viewModel.hcStepList.value.isNotEmpty() && viewModel.hcWeightList.value.isNotEmpty()) {
//                                hcExerciseGlucoseList.addAll(viewModel.hcExerciseList.value)
//                                hcExerciseGlucoseList.addAll(viewModel.hcGlucoseList.value)
//                                viewModel.setHcExerciseGlucoseList(hcExerciseGlucoseList)
//                                Log.e(
//                                    "zip1",
//                                    viewModel.hcExerciseList.value.toString() + viewModel.hcGlucoseList.value.toString()
//                                )
//                            }
//                        }
//                    }
//
//                    lifecycleScope.launch {
//                        viewModel.hcStepList.zip(viewModel.hcWeightList) { stepList, weightList ->
//                            Log.e("stepList", stepList.toString())
//                            Log.e("weightList", weightList.toString())
//                        }.collect {
//                            if (viewModel.hcStepList.value.isNotEmpty() && viewModel.hcWeightList.value.isNotEmpty()) {
//                                hcWeightStepList.addAll(viewModel.hcStepList.value)
//                                hcWeightStepList.addAll(viewModel.hcWeightList.value)
//                                viewModel.setHcWeightStepList(hcWeightStepList)
//                                Log.e(
//                                    "zip2",
//                                    viewModel.hcStepList.value.toString() + viewModel.hcWeightList.value.toString()
//                                )
//                            }
//                        }
//                    }
//
//                    lifecycleScope.launch {
//                        viewModel.hcExerciseGlucoseList.zip(viewModel.hcWeightStepList) { exerciseGlucoseList, weightStepList ->
//                            Log.e("exerciseGlucoseList", exerciseGlucoseList.toString())
//                            Log.e("weightStepList", weightStepList.toString())
//                        }.collect {
//                            if (viewModel.hcExerciseGlucoseList.value.isNotEmpty() && viewModel.hcWeightStepList.value.isNotEmpty()) {
//                                getAccessToken()?.let { it1 -> viewModel.postHealthConnectData(it1) }
//                            }
//                        }
//                    }
//
//                    lifecycleScope.launch {
//                        //TODO : API Response 받은 후 화면 이동 처리 -> 현재 안됨
//                        viewModel.postHealthConnectFlow.collect {
//                            //goToMainOrLoginActivity()
//                        }
//                    }
//                }
//            }
//
//        } else if (healthConnectAvailability == "false") {
//            goToMainOrLoginActivity()
//            if (viewModel.healthConnectFlow.value.isAvaiable == HEALTH_CONNECT_NOT_INSTALLED) {
//                //TODO : 헬스커넥트 팝업 띄우기
//            }
//        }

    }

    private fun goToMainOrLoginActivity() {
        if (viewModel.getAutoLoginProviderSpf() == KAKAO || viewModel.getAutoLoginProviderSpf() == NAVER) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun getAccessToken(): String? {
        val sharedPreferences = getSharedPreferences(TOKEN_SPF_KEY, Context.MODE_PRIVATE)
        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
    }

    private fun setIntroVersionName(introVersionName: String) {
        val sharedPreferences = applicationContext.getSharedPreferences(VERSION_SPF_KEY, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(VERSION_INTRO_TOKEN_KEY, introVersionName).apply()
    }

}