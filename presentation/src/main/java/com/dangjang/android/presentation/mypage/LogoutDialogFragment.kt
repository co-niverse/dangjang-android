package com.dangjang.android.presentation.mypage

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.dangjang.android.presentation.databinding.FragmentLogoutDialogBinding

class LogoutDialogFragment: DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true
    }

    private lateinit var binding: FragmentLogoutDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLogoutDialogBinding.inflate(inflater,container,false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.logoutBtn.setOnClickListener {
            //TODO : 로그아웃 API 연동
        }

        binding.dismissBtn.setOnClickListener {
            dismiss()
        }
    }
}