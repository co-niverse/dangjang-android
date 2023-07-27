package com.dangjang.android.presentation

import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.viewModels
import com.dangjang.android.common_ui.BaseFragment
import com.dangjang.android.presentation.databinding.FragmentSignupBodyBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupBodyFragment : BaseFragment<FragmentSignupBodyBinding>(R.layout.fragment_signup_body) {

    private val viewModel by viewModels<SignupViewModel>()

    override fun initView() {
        bind {
            vm = viewModel
        }
    }
    override fun onStart() {
        super.onStart()

        binding.bodyBtn.setOnTouchListener({ v, event -> true })

        var heightFlag = false
        var weightFlag = false

        binding.heightEt.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                heightFlag = true
                if (heightFlag && weightFlag) {
                    setBodyGreen()
                }
            }

        })

        binding.weightEt.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                weightFlag = true
                if (heightFlag && weightFlag) {
                    setBodyGreen()
                }
            }

        })

        binding.bodyBtn.setOnClickListener {
            val signupActiveFragment = SignupActiveFragment()
            parentFragmentManager.beginTransaction().replace(R.id.fragment_signup_view, signupActiveFragment).addToBackStack(null).commit()
        }

        binding.backIv.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

    }

    private fun setBodyGreen() {
        binding.bodyBtn.setBackgroundResource(R.drawable.background_green_gradient)
        binding.bodyBtn.setOnTouchListener({ v, event -> false })
    }
}