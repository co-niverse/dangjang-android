package com.dangjang.android.presentation.home

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.dangjang.android.presentation.databinding.FragmentHealthconnectBottomSheetBinding
import com.dangjang.android.presentation.mypage.DeviceActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class HealthConnectBottomSheetFragment: BottomSheetDialogFragment() {

    private val viewModel by activityViewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true
    }

    private lateinit var binding: FragmentHealthconnectBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHealthconnectBottomSheetBinding.inflate(inflater,container,false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dismissBtn.setOnClickListener {
            viewModel.shotHealthConnectJoinClickLogging(false)
            dismiss()
        }

        binding.goToHealthConnectFragmentButton.setOnClickListener {
            viewModel.shotHealthConnectJoinClickLogging(true)
            dismiss()
            Intent(context, DeviceActivity::class.java).apply {
                startActivity(this)
            }
        }
    }
}