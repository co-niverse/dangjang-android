package com.dangjang.android.presentation.mypage

import android.content.Context
import android.content.Intent
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.dangjang.android.common_ui.BaseFragment
import com.dangjang.android.domain.constants.ACCESS_TOKEN_KEY
import com.dangjang.android.domain.constants.TOKEN_SPF_KEY
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.FragmentPointCheckBinding
import com.dangjang.android.presentation.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class PointCheckFragment : BaseFragment<FragmentPointCheckBinding>(R.layout.fragment_point_check){

    private val viewModel : MypageViewModel by activityViewModels()

    override fun initView() {
        bind {
            vm = viewModel
        }
    }

    override fun onStart() {
        super.onStart()

        lifecycleScope.launchWhenStarted {
            viewModel.goToLoginActivityFlow.collectLatest {
                if (it) {
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
            }
        }

        val type = viewModel.selectedGiftTitle.value
        val price = viewModel.selectedGiftPrice.value
        val phone = viewModel.selectedGiftPhone.value
        val name = viewModel.selectedGiftName.value
        val comment = viewModel.selectedGiftComment.value

        binding.giftTitleTv.text = type
        binding.giftPointTv.text = price + " 포인트"
        binding.phoneTv.text = formatPhoneNumber(phone)
        binding.nameTv.text = name
        binding.commentTv.text = comment

        viewModel.setPostPointRequest(type, formatPhoneNumber(phone), name, comment)

        binding.backIv.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.nextBtn.setOnClickListener {
            getAccessToken()?.let { viewModel.postPoint(it) }
            val pointDoneFragment = PointDoneFragment()
            parentFragmentManager.beginTransaction().replace(R.id.point_top_cl, pointDoneFragment).addToBackStack(null).commit()
        }
    }

    private fun formatPhoneNumber(phone: String): String {
        val regex = "(\\d{3})(\\d{4})(\\d{4})".toRegex()
        return phone.replace(regex, "$1-$2-$3")
    }

    private fun getAccessToken(): String? {
        val sharedPreferences = requireContext().getSharedPreferences(TOKEN_SPF_KEY, Context.MODE_PRIVATE)
        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
    }

}