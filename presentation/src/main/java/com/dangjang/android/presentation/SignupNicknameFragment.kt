package com.dangjang.android.presentation

import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.viewModels
import com.dangjang.android.common_ui.BaseFragment
import com.dangjang.android.presentation.databinding.FragmentSignupNicknameBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupNicknameFragment : BaseFragment<FragmentSignupNicknameBinding>(R.layout.fragment_signup_nickname) {

    private val viewModel by viewModels<SignupViewModel>()

    override fun initView() {
        bind {
            vm = viewModel
        }
    }
    override fun onStart() {
        super.onStart()

        binding.nicknameEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0!!.length > 8) {
                    binding.warnTv.text = "8글자 이내로 작성해주세요"
                    binding.warnTv.setTextColor(Color.RED)
                    binding.nicknameBtn.setBackgroundResource(R.drawable.background_round_darkgray)
                } else if (p0!!.isNotEmpty()) {
                    binding.nicknameTextTv.setTextColor(Color.parseColor("#41E551"))
                    binding.warnTv.text = "사용 가능한 닉네임이에요."
                    binding.warnTv.setTextColor(Color.parseColor("#41E551"))
                    binding.nicknameBtn.setBackgroundResource(R.drawable.background_green_gradient)
                }
                else if (p0!!.isEmpty()) {
                    binding.warnTv.text = ""
                    binding.warnTv.setTextColor(Color.parseColor("#41E551"))
                    binding.nicknameBtn.setBackgroundResource(R.drawable.background_round_darkgray)
                }
            }

        })

    }

}