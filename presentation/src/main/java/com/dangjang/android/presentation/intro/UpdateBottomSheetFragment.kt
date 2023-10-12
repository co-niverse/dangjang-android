package com.dangjang.android.presentation.intro

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dangjang.android.domain.constants.PLAY_STORE_URL
import com.dangjang.android.presentation.databinding.FragmentUpdateBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class UpdateBottomSheetFragment : BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true
    }

    private lateinit var binding: FragmentUpdateBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateBottomSheetBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.closeBtn.setOnClickListener {
            dismiss()
        }

        binding.updateBtn.setOnClickListener {
            dismiss()
            goToPlayStore()
        }
    }

    private fun goToPlayStore() {
        val intentUrl = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(PLAY_STORE_URL)
        )
        startActivity(intentUrl)
    }
}