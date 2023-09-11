package com.dangjang.android.presentation.home

import android.content.Intent
import android.content.SharedPreferences
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.dangjang.android.common_ui.BaseFragment
import com.dangjang.android.domain.constants.AUTO_LOGIN_SPF_KEY
import com.dangjang.android.domain.constants.HEALTH_CONNECT_TOKEN_KEY
import com.dangjang.android.domain.model.GlucoseGuideVO
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var glucoseGuideAdapter: GlucoseGuideAdapter
    private var glucoseGuideList = arrayListOf<GlucoseGuideVO>()

    override fun initView() {
        bind {
            vm = viewModel
        }
    }
    override fun onStart() {
        super.onStart()

        binding.weightSeekbar.setOnTouchListener({ v, event -> true })

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

        glucoseGuideList.add(GlucoseGuideVO("저혈당","1번",R.drawable.background_circle_red))
        glucoseGuideList.add(GlucoseGuideVO("저혈당\n의심","1번",R.drawable.background_circle_orange))
        glucoseGuideList.add(GlucoseGuideVO("정상","1번",R.drawable.background_circle_green))
        glucoseGuideList.add(GlucoseGuideVO("주의","1번",R.drawable.background_circle_orange))
        glucoseGuideList.add(GlucoseGuideVO("경고","1번",R.drawable.background_circle_red))

        setGlucoseGuideListAdapter()
    }

    private fun setGlucoseGuideListAdapter() {
        glucoseGuideAdapter = GlucoseGuideAdapter(glucoseGuideList)

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
}