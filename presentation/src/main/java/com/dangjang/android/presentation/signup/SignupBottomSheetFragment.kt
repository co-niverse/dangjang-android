package com.dangjang.android.presentation.signup

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.dangjang.android.presentation.databinding.FragmentSignupBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SignupBottomSheetFragment : BottomSheetDialogFragment() {

    private val viewModel by activityViewModels<SignupViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true
    }

    private lateinit var binding: FragmentSignupBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupBottomSheetBinding.inflate(inflater, container, false)

        binding.vm = viewModel
        binding.lifecycleOwner = this

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        viewModel.getSduiSignup()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dismissBtn.setOnClickListener {
            dismiss()
        }

        binding.exitBtn.setOnClickListener {
            activity?.finish()
        }
    }

}