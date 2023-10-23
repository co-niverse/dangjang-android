package com.dangjang.android.presentation.signup

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.dangjang.android.common_ui.BaseFragment
import com.dangjang.android.domain.HttpResponseStatus
import com.dangjang.android.domain.constants.NEW_USER_KEY
import com.dangjang.android.presentation.MainActivity
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.FragmentSignupAgreeBinding
import com.dangjang.android.presentation.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignupAgreeFragment : BaseFragment<FragmentSignupAgreeBinding>(R.layout.fragment_signup_agree) {

    private val viewModel : SignupViewModel by activityViewModels()

    override fun initView() {
        bind {
            vm = viewModel
        }
    }
    override fun onStart() {
        super.onStart()

        binding.agreeBtn.setOnTouchListener({ v, event -> true })

        var serviceFlag = false
        var personalFlag = false

        lifecycleScope.launch {
            viewModel.goToLoginActivity.collectLatest {
                if (it) {
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
            }
        }

        binding.serviceCl.setOnClickListener {
            binding.serviceTv.setTextColor((Color.BLACK))
            binding.serviceCheckIv.setImageResource(R.drawable.ic_check_green)
            serviceFlag = true

            if (personalFlag) {
                setBtnGreen()
            }
        }

        binding.serviceCheckIv.setOnClickListener {
            binding.serviceTv.setTextColor((Color.BLACK))
            binding.serviceCheckIv.setImageResource(R.drawable.ic_check_green)
            serviceFlag = true

            if (personalFlag) {
                setBtnGreen()
            }
        }

        binding.personalCl.setOnClickListener {
            binding.personalTv.setTextColor((Color.BLACK))
            binding.personalCheckIv.setImageResource(R.drawable.ic_check_green)
            personalFlag = true

            if (serviceFlag) {
                setBtnGreen()
            }
        }

        binding.personalCheckIv.setOnClickListener {
            binding.personalTv.setTextColor((Color.BLACK))
            binding.personalCheckIv.setImageResource(R.drawable.ic_check_green)
            personalFlag = true

            if (serviceFlag) {
                setBtnGreen()
            }
        }

        binding.agreeBtn.setOnClickListener {
            Log.e("viewmodel",viewModel.signupRequest.value.toString())
            viewModel.signup(viewModel.signupRequest.value)
            lifecycleScope.launchWhenStarted {
                viewModel.startMainActivity.collect{
                    if(it == HttpResponseStatus.OK){
                        startActivity(Intent(context, MainActivity::class.java))
                        activity?.finish()
                    }
                }
            }
            //TODO : 회원가입 유저라는거 저장
            saveNewUserSpf(NEW_USER_KEY, "newUser")
        }

        binding.serviceUrlTv.setOnClickListener {
            goToServiceUrl()
        }

        binding.privateUrlTv.setOnClickListener {
            goToPrivateUrl()
        }

        binding.backIv.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setBtnGreen() {
        binding.agreeBtn.setBackgroundResource(R.drawable.background_green_gradient)
        binding.agreeBtn.setOnTouchListener({ v, event -> false })
    }

    private fun goToServiceUrl() {
        val intentUrl = Intent(Intent.ACTION_VIEW, Uri.parse("https://co-niverse.notion.site/a3e0b9b54d0f4b3bba174901297ec918?pvs=4"))
        startActivity(intentUrl)
    }

    private fun goToPrivateUrl() {
        val intentUrl = Intent(Intent.ACTION_VIEW, Uri.parse("https://co-niverse.notion.site/cb58f28f2c6e465ea5c596871baaca78?pvs=4"))
        startActivity(intentUrl)
    }

    private fun saveNewUserSpf(key: String, newUser: String?) {
        val sharedPreferences = requireContext().getSharedPreferences(NEW_USER_KEY, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(key, newUser).apply()
    }

}