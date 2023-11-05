package com.dangjang.android.presentation.home

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.dangjang.android.domain.constants.ACCESS_TOKEN_KEY
import com.dangjang.android.domain.constants.TOKEN_SPF_KEY
import com.dangjang.android.presentation.databinding.FragmentGlucoseDeleteDialogBinding
import com.dangjang.android.presentation.login.LoginActivity
import kotlinx.coroutines.flow.collectLatest

class GlucoseDeleteDialogFragment : DialogFragment(
) {
    private lateinit var binding: FragmentGlucoseDeleteDialogBinding
    private val viewModel by activityViewModels<HomeViewModel>()
    private var time: String = ""
    private var date = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGlucoseDeleteDialogBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        time = arguments?.getString("time").toString()
        date = arguments?.getString("date").toString()


        lifecycleScope.launchWhenStarted {
            viewModel.goToLoginActivityFlow.collectLatest {
                if (it) {
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dismissBtn.setOnClickListener {
            dismiss()
        }

        binding.deleteBtn.setOnClickListener {
            //TODO : 삭제하기 API 호출
            Log.e("삭제하기","삭제하기 API 호출")
//            viewModel.setEditGlucoseCreatedAt(date)
//            viewModel.setEditGlucoseNewType(newType)
//            viewModel.setEditGlucoseType(time)
//            viewModel.setEditGlucoseValue(getGlucose())
//            getAccessToken()?.let { viewModel.editGlucose(it) }
            dismiss()
        }
    }

    private fun getAccessToken(): String? {
        val sharedPreferences = requireContext().getSharedPreferences(TOKEN_SPF_KEY, Context.MODE_PRIVATE)
        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
    }

}