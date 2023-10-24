package com.dangjang.android.presentation.mypage

import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.dangjang.android.common_ui.BaseFragment
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.FragmentPointPhoneBinding
import com.dangjang.android.presentation.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class PointPhoneFragment : BaseFragment<FragmentPointPhoneBinding>(R.layout.fragment_point_phone){

    private val viewModel : MypageViewModel by activityViewModels()
    private var nameFlag = false
    private var phoneFlag = false

    override fun initView() {
        bind {
            vm = viewModel
        }
    }

    override fun onStart() {
        super.onStart()

        val type = viewModel.selectedGiftTitle.value
        val price = viewModel.selectedGiftPrice.value

        binding.giftTitleTv.text = type
        binding.giftPointTv.text = price + " 포인트"

        binding.nextBtn.setOnTouchListener({ v, event -> true })

        lifecycleScope.launchWhenStarted {
            viewModel.goToLoginActivityFlow.collectLatest {
                if (it) {
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
            }
        }

        binding.backIv.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.nameEt.addTextChangedListener(object : android.text.TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: android.text.Editable?) {
                nameFlag = true
                setBtnGreen()
            }

        })

        binding.phoneEt.addTextChangedListener(object : android.text.TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: android.text.Editable?) {
                phoneFlag = true
                setBtnGreen()
            }

        })

        binding.nextBtn.setOnClickListener {
            if (binding.phoneEt.text.startsWith("010")) {
                val pointCheckFragment = PointCheckFragment()
                viewModel.setSelectedGiftPhone(binding.phoneEt.text.toString())
                viewModel.setSelectedGiftName(binding.nameEt.text.toString())
                viewModel.setSelectedGiftComment(binding.commentEt.text.toString())
                parentFragmentManager.beginTransaction().replace(R.id.point_top_cl, pointCheckFragment).addToBackStack(null).commit()
            } else {
                Toast.makeText(requireContext(), "휴대폰 번호는 010으로 시작해야 합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setBtnGreen() {
        if (nameFlag && phoneFlag) {
            binding.nextBtn.setBackgroundResource(R.drawable.background_green_gradient)
            binding.nextBtn.setOnTouchListener({ v, event -> false })
        }
    }

}