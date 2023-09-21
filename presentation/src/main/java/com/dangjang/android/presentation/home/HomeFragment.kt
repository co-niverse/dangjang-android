package com.dangjang.android.presentation.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.dangjang.android.common_ui.BaseFragment
import com.dangjang.android.domain.constants.AUTO_LOGIN_SPF_KEY
import com.dangjang.android.domain.constants.HEALTH_CONNECT_TOKEN_KEY
import com.dangjang.android.domain.constants.ACCESS_TOKEN_KEY
import com.dangjang.android.domain.constants.BMI_NORMAL_END
import com.dangjang.android.domain.constants.BMI_NORMAL_START
import com.dangjang.android.domain.constants.SEEKBAR_NORMAL_END
import com.dangjang.android.domain.constants.SEEKBAR_NORMAL_START
import com.dangjang.android.domain.constants.TOKEN_SPF_KEY
import com.dangjang.android.domain.model.GlucoseGuideVO
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var glucoseGuideAdapter: GlucoseGuideAdapter
    override fun initView() {
        bind {
            vm = viewModel
        }
    }
    override fun onStart() {
        super.onStart()

        binding.vm = viewModel
        binding.lifecycleOwner = this

        getAccessToken()?.let { viewModel.getHome(it, viewModel.getTodayDate()) }

        binding.weightSeekbar.setOnTouchListener({ v, event -> true })

        lifecycleScope.launchWhenStarted {
            viewModel.getHomeFlow.collectLatest {
                if (it.bloodSugars.isNullOrEmpty()) {
                    binding.glucoseGuideNoneTv.visibility = View.VISIBLE
                }
                else {
                    binding.glucoseGuideNoneTv.visibility = View.GONE
                }
                val bloodSugarsList = viewModel.addBackgroundToTodayGuides(it.bloodSugars)
                glucoseGuideAdapter.submitList(bloodSugarsList)

                binding.weightSeekbar.progress = calculateSeekbarProgress(it.weight.bmi)

                if (it.weight.unit == "") {
                    binding.weightNoneTv.visibility = View.VISIBLE
                } else {
                    binding.weightNoneTv.visibility = View.GONE
                }

            }
        }

        binding.glucoseCl.setOnClickListener {
            Intent(activity, GlucoseActivity::class.java).apply {
                startActivity(this)
            }
        }

        binding.weightCl.setOnClickListener {
            Intent(activity, WeightActivity::class.java).apply {
                startActivity(this)
            }
        }

        binding.exerciseCl.setOnClickListener {
            Intent(activity, ExerciseActivity::class.java).apply {
                startActivity(this)
            }
        }

        val sp: SharedPreferences = requireContext().getSharedPreferences(AUTO_LOGIN_SPF_KEY, AppCompatActivity.MODE_PRIVATE)
        val healthConnect = sp.getString(HEALTH_CONNECT_TOKEN_KEY, "null")

        if (healthConnect == "false") {
            binding.autoInputBtn.visibility = View.VISIBLE
            binding.autoInputBtn.setOnClickListener {
                HealthConnectBottomSheetFragment().show(parentFragmentManager, "HealthConnectBottomSheetFragment")
            }
        }

        setGlucoseGuideListAdapter()
    }

    private fun setGlucoseGuideListAdapter() {
        glucoseGuideAdapter = GlucoseGuideAdapter(viewModel)

        glucoseGuideAdapter.setMyItemClickListener(object :
            GlucoseGuideAdapter.MyItemClickListener {
            override fun onItemClick(glucoseGuideList: GlucoseGuideVO) {
                Intent(activity, GlucoseActivity::class.java).apply {
                    startActivity(this)
                }
            }

        })

        binding.homeGlucoseRv.adapter = glucoseGuideAdapter
    }

    private fun getAccessToken(): String? {
        val sharedPreferences = requireContext().getSharedPreferences(TOKEN_SPF_KEY, Context.MODE_PRIVATE)

        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
    }

    private fun calculateSeekbarProgress(bmi: Double): Int {
        val progress: Double = if (bmi < BMI_NORMAL_START) {
            SEEKBAR_NORMAL_START - (BMI_NORMAL_START - bmi)
        } else if (bmi in BMI_NORMAL_START..BMI_NORMAL_END) {
            SEEKBAR_NORMAL_START + (bmi - BMI_NORMAL_START)*((SEEKBAR_NORMAL_END - SEEKBAR_NORMAL_START)/(BMI_NORMAL_END - BMI_NORMAL_START))
        } else {
            SEEKBAR_NORMAL_END + (bmi - BMI_NORMAL_END)
        }

        return progress.toInt()
    }
}