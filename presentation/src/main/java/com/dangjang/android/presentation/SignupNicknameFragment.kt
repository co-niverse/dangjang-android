package com.dangjang.android.presentation

import android.graphics.Color
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import androidx.fragment.app.activityViewModels
import com.dangjang.android.common_ui.BaseFragment
import com.dangjang.android.presentation.databinding.FragmentSignupNicknameBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern


@AndroidEntryPoint
class SignupNicknameFragment : BaseFragment<FragmentSignupNicknameBinding>(R.layout.fragment_signup_nickname) {

    private val viewModel : SignupViewModel by activityViewModels()

    override fun initView() {
        bind {
            vm = viewModel
        }
    }
    override fun onStart() {
        super.onStart()

        binding.nicknameBtn.setOnTouchListener({ v, event -> true })

        binding.nicknameEt.setFilters(arrayOf(
            InputFilter { src, start, end, dst, dstart, dend ->
                val ps = Pattern.compile("^[a-zA-Z0-9ㄱ-ㅎ가-흐ㄱ-ㅣ가-힣ᆢᆞ\\u318d\\u119E\\u11A2\\u2022\\u2025a\\u00B7\\uFE55\\s]+$")
                if (!ps.matcher(src).matches()) {
                    return@InputFilter ""
                } else {
                    return@InputFilter null
                }
            }
        ))

        binding.nicknameEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {

                viewModel.getDuplicateNickname(p0.toString())

                if (viewModel.duplicateNicknameFlow.value.duplicate) {
                    if (p0!!.length > 8) {
                        binding.warnTv.text = "8글자 이내로 작성해주세요"
                        binding.warnTv.setTextColor(Color.RED)
                        setBtnGray()
                    } else if (p0!!.isNotEmpty()) {
                        binding.nicknameTextTv.setTextColor(Color.parseColor("#41E551"))
                        binding.warnTv.text = "사용 가능한 닉네임이에요."
                        binding.warnTv.setTextColor(Color.parseColor("#41E551"))
                        setBtnGreen()
                    }
                    else if (p0!!.isEmpty()) {
                        binding.warnTv.text = ""
                        binding.warnTv.setTextColor(Color.parseColor("#41E551"))
                        setBtnGray()
                    }
                } else {
                    binding.nicknameTextTv.setTextColor(Color.RED)
                    binding.warnTv.text = "이미 사용중인 닉네임이에요."
                    binding.warnTv.setTextColor(Color.RED)
                    setBtnGreen()
                }
            }

        })

        binding.nicknameBtn.setOnClickListener {

            viewModel.setNickname(binding.nicknameEt.text.toString())

            val signupGenderBirthFragment = SignupGenderBirthFragment()
            parentFragmentManager.beginTransaction().replace(R.id.fragment_signup_view, signupGenderBirthFragment).addToBackStack(null).commit()
        }

    }

    private fun setBtnGreen() {
        binding.nicknameBtn.setBackgroundResource(R.drawable.background_green_gradient)
        binding.nicknameBtn.setOnTouchListener({ v, event -> false })
    }

    private fun setBtnGray() {
        binding.nicknameBtn.setBackgroundResource(R.drawable.background_round_darkgray)
        binding.nicknameBtn.setOnTouchListener({ v, event -> true })
    }

}