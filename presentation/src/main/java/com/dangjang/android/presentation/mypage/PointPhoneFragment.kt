package com.dangjang.android.presentation.mypage

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.dangjang.android.common_ui.BaseFragment
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.FragmentPointPhoneBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PointPhoneFragment : BaseFragment<FragmentPointPhoneBinding>(R.layout.fragment_point_phone){

    private val viewModel : MypageViewModel by activityViewModels()

    override fun initView() {
        bind {
            vm = viewModel
        }
    }

    override fun onStart() {
        super.onStart()

        val type = arguments?.getString("type").toString()
        val price = arguments?.getString("price").toString()

        binding.giftTitleTv.text = type
        binding.giftPointTv.text = price + " 포인트"

        binding.nextBtn.setOnTouchListener({ v, event -> true })

        binding.backIv.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.phoneEt.addTextChangedListener(object : android.text.TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: android.text.Editable?) {
                setBtnGreen()
                binding.nextBtn.setOnTouchListener({ v, event -> false })
            }

        })

        binding.nextBtn.setOnClickListener {
            val pointCheckFragment = PointCheckFragment()
            var bundle = Bundle()
            bundle.putString("type", type)
            bundle.putString("price", price)
            bundle.putString("phone", binding.phoneEt.text.toString())
            pointCheckFragment.arguments = bundle
            parentFragmentManager.beginTransaction().replace(R.id.point_cl, pointCheckFragment).addToBackStack(null).commit()
        }
    }

    private fun setBtnGreen() {
        binding.nextBtn.setBackgroundResource(R.drawable.background_green_gradient)
        binding.nextBtn.setOnTouchListener({ v, event -> false })
    }

}