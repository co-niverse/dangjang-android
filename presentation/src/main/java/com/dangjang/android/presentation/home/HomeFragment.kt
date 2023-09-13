package com.dangjang.android.presentation.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.dangjang.android.common_ui.BaseFragment
import com.dangjang.android.domain.constants.ACCESS_TOKEN_KEY
import com.dangjang.android.domain.constants.AUTO_LOGIN_SPF_KEY
import com.dangjang.android.domain.constants.HEALTH_CONNECT_TOKEN_KEY
import com.dangjang.android.domain.constants.TOKEN_SPF_KEY
import com.dangjang.android.domain.model.GlucoseGuideVO
import com.dangjang.android.domain.model.TodayGuidesVO
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


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

        getAccessToken()?.let {
                accessToken -> viewModel.getGlucose(accessToken, getTodayDate())
        }

        lifecycleScope.launchWhenStarted {
            viewModel.getGlucoseFlow.collectLatest {
                if (it.todayGuides.isNullOrEmpty()) {
                    binding.glucoseGuideNoneTv.visibility = View.VISIBLE
                }
                else {
                    binding.glucoseGuideNoneTv.visibility = View.GONE
                }
                val todayGuidesList = viewModel.addBackgroundToTodayGuides(it.todayGuides)
                glucoseGuideAdapter.submitList(deleteZeroCount(todayGuidesList))
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

        glucoseGuideList.add(GlucoseGuideVO("저혈당","1번",R.drawable.background_circle_red))
        glucoseGuideList.add(GlucoseGuideVO("저혈당\n의심","1번",R.drawable.background_circle_orange))
        glucoseGuideList.add(GlucoseGuideVO("정상","1번",R.drawable.background_circle_green))
        glucoseGuideList.add(GlucoseGuideVO("주의","1번",R.drawable.background_circle_orange))
        glucoseGuideList.add(GlucoseGuideVO("경고","1번",R.drawable.background_circle_red))

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

    private fun getTodayDate(): String {
        val currentTime: Date = Calendar.getInstance().getTime()
        val format = SimpleDateFormat("yyyy-MM-dd")
        return format.format(currentTime)
    }

    private fun deleteZeroCount(todayGuidesList: List<GlucoseGuideVO>): MutableList<GlucoseGuideVO> {
        var todayGuidesCountedList = mutableListOf<GlucoseGuideVO>()
        todayGuidesList.map {
            if (it.guideCount != "0번") {
                todayGuidesCountedList.add(it)
            }
        }
        return todayGuidesCountedList
    }
}