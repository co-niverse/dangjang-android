package com.dangjang.android.presentation.signup

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.dangjang.android.common_ui.BaseFragment
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.FragmentSignupNicknameBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.regex.Pattern


@AndroidEntryPoint
class SignupNicknameFragment :
    BaseFragment<FragmentSignupNicknameBinding>(R.layout.fragment_signup_nickname) {

    private val viewModel: SignupViewModel by activityViewModels()
    private var startTime: Double = 0.0
    private var endTime: Double = 0.0
    private lateinit var callback: OnBackPressedCallback

    override fun initView() {
        bind {
            vm = viewModel
        }
    }

    override fun onStart() {
        super.onStart()
        startTime = System.currentTimeMillis().toDouble()

        binding.nicknameBtn.setOnTouchListener({ v, event -> true })

        binding.nicknameEt.setFilters(arrayOf(
            InputFilter { src, start, end, dst, dstart, dend ->
                val ps =
                    Pattern.compile("^[a-zA-Z0-9ㄱ-ㅎ가-흐ㄱ-ㅣ가-힣ᆢᆞ\\u318d\\u119E\\u11A2\\u2022\\u2025a\\u00B7\\uFE55\\s]+$")
                if (!ps.matcher(src).matches()) {
                    return@InputFilter ""
                } else {
                    return@InputFilter null
                }
            },
            InputFilter.LengthFilter(8)
        ))

        binding.nicknameEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                setBtnGray()
                binding.warnTv.text = ""
                binding.nicknameTextTv.setTextColor(Color.parseColor("#666666"))
            }

        })

        lifecycleScope.launch {
            viewModel.duplicateNicknameFlow.collectLatest {
                Log.e("duplicateNicknameFlow", it.toString())
                if (it.duplicate == "true") {
                    binding.nicknameTextTv.setTextColor(Color.parseColor("#41E551"))
                    binding.warnTv.text = "사용 가능한 닉네임이에요."
                    binding.warnTv.setTextColor(Color.parseColor("#41E551"))
                    setBtnGreen()
                } else if (it.duplicate == "false") {
                    binding.nicknameTextTv.setTextColor(Color.RED)
                    binding.warnTv.text = "이미 사용중인 닉네임이에요."
                    binding.warnTv.setTextColor(Color.RED)
                    setBtnGray()
                }
            }
        }

        binding.duplicateNicknameBtn.setOnClickListener {
            viewModel.getDuplicateNickname(binding.nicknameEt.text.toString())
        }


        binding.nicknameBtn.setOnClickListener {

            viewModel.setNickname(binding.nicknameEt.text.toString())

            val signupGenderBirthFragment = SignupGenderBirthFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_signup_view, signupGenderBirthFragment).addToBackStack(null)
                .commit()
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                SignupBottomSheetFragment().show(parentFragmentManager, "SignupBottomSheetFragment")
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        endTime = System.currentTimeMillis().toDouble()
        viewModel.shotSignupNicknameLogging(endTime- startTime)
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