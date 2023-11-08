package com.dangjang.android.presentation.signup

import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.activityViewModels
import com.dangjang.android.common_ui.BaseFragment
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.FragmentSignupBodyBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupBodyFragment : BaseFragment<FragmentSignupBodyBinding>(R.layout.fragment_signup_body) {

    private val viewModel : SignupViewModel by activityViewModels()
    private var height : Int = 0
    private var weight : Int = 0
    private var startTime: Double = 0.0
    private var endTime: Double = 0.0

    override fun initView() {
        bind {
            vm = viewModel
        }
    }
    override fun onStart() {
        super.onStart()
        startTime = System.currentTimeMillis().toDouble()

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
                if (p0.toString() != "") {
                    height = p0.toString().toInt()
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
                if (p0.toString() != "") {
                    weight = p0.toString().toInt()
                }
            }

        })

        binding.bodyBtn.setOnClickListener {
            viewModel.setBody(height, weight)

            val signupActiveFragment = SignupActiveFragment()
            parentFragmentManager.beginTransaction().replace(R.id.fragment_signup_view, signupActiveFragment).addToBackStack(null).commit()
        }

        binding.backIv.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        endTime = System.currentTimeMillis().toDouble()
        viewModel.shotSignupBodyLogging(endTime- startTime)
    }

    private fun setBodyGreen() {
        binding.bodyBtn.setBackgroundResource(R.drawable.background_green_gradient)
        binding.bodyBtn.setOnTouchListener({ v, event -> false })
    }
}