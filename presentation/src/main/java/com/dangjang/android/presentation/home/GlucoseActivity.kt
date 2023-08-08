package com.dangjang.android.presentation.home

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.dangjang.android.domain.model.GlucoseListVO
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.ActivityGlucoseBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GlucoseActivity : FragmentActivity() {
    private lateinit var binding: ActivityGlucoseBinding
    private lateinit var viewModel: GlucoseViewModel
    private lateinit var glucoseListAdapter: GlucoseListAdapter
    private var glucoseList = arrayListOf<GlucoseListVO>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glucose)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_glucose)
        viewModel = ViewModelProvider(this).get(GlucoseViewModel::class.java)

        binding.glucoseAddBtn.setOnClickListener {
            binding.glucoseAddCl.visibility = View.VISIBLE
        }

        binding.glucoseAddCloseIv.setOnClickListener {
            binding.glucoseAddCl.visibility = View.GONE
        }

        glucoseList.add(
                GlucoseListVO(
                    "공복",
                    100,
                    "test",
                    "test"))
        glucoseList.add(
                GlucoseListVO(
                    "공복",
                    100,
                    "test",
                    "test"))
        glucoseList.add(
                GlucoseListVO(
                    "공복",
                    100,
                    "test",
                    "test")
        )
        setGlucoseListAdapter()

    }

    private fun setGlucoseListAdapter() {
        glucoseListAdapter = GlucoseListAdapter(glucoseList)

        glucoseListAdapter.setMyItemClickListener(object :
            GlucoseListAdapter.MyItemClickListener {
            override fun onItemClick(glucoseList: GlucoseListVO) {
                // TODO : 클릭했을 때 UI 변경 - selector ?
            }
        })

        binding.glucoseRv.adapter = glucoseListAdapter
    }


}