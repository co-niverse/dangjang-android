package com.dangjang.android.presentation.mypage

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.dangjang.android.domain.constants.ACCESS_TOKEN_KEY
import com.dangjang.android.domain.constants.FCM_TOKEN_KEY
import com.dangjang.android.domain.constants.TOKEN_SPF_KEY
import com.dangjang.android.domain.request.LogoutRequest
import com.dangjang.android.presentation.databinding.FragmentLogoutDialogBinding
import com.dangjang.android.presentation.login.LoginActivity
import kotlinx.coroutines.flow.collectLatest

class LogoutDialogFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true
    }

    private lateinit var binding: FragmentLogoutDialogBinding
    private val viewModel by activityViewModels<MypageViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLogoutDialogBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.goToLoginActivityFlow.collectLatest {
                if (it) {
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
            }
        }

        binding.logoutBtn.setOnClickListener {
            viewModel.logout(getAccessToken() ?: "", LogoutRequest(getFCMToken() ?: ""))
            lifecycleScope.launchWhenStarted {
                viewModel.logoutFlow.collectLatest {
                    if (it) {
                        Toast.makeText(requireContext(), "로그아웃이 완료되었습니다.", Toast.LENGTH_SHORT).show()

                        viewModel.removeAutoLoginProviderSpf()
                        viewModel.removeAccessTokenSpf()
                        viewModel.removeFcmTokenSpf()

                        val intent = Intent(requireContext(), LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)

                        activity?.finish()
                    } else {
//                        Toast.makeText(requireContext(), "다시 한번 시도해주세요.", Toast.LENGTH_SHORT)
//                            .show()
//                        dismiss()
                    }
                }
            }
        }

        binding.dismissBtn.setOnClickListener {
            dismiss()
        }

    }

    private fun getFCMToken(): String? {
        val sharedPreferences = requireContext().getSharedPreferences(FCM_TOKEN_KEY, Context.MODE_PRIVATE)
        return sharedPreferences.getString(FCM_TOKEN_KEY, null)
    }

    private fun getAccessToken(): String? {
        val sharedPreferences = requireContext().getSharedPreferences(TOKEN_SPF_KEY, Context.MODE_PRIVATE)
        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
    }
}