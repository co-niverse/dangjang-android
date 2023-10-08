package com.dangjang.android.presentation.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.dangjang.android.domain.constants.ACCESS_TOKEN_KEY
import com.dangjang.android.domain.constants.TOKEN_SPF_KEY
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.ActivityWeightBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class WeightActivity : FragmentActivity() {
    private lateinit var binding: ActivityWeightBinding
    private lateinit var viewModel: HomeViewModel
    private var originWeight: Int = 0
    private var date = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weight)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_weight)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        binding.vm = viewModel

        binding.lifecycleOwner = this

        viewModel.shotWeightClickLogging()

        date = intent.getStringExtra("date").toString()

        getAccessToken()?.let { viewModel.getWeight(it, date) }

        binding.weightSeekbar.setOnTouchListener({ v, event -> true })
        
        //TODO : lifecyelceScope에 없어도 되는지 테스트 해보기
        lifecycleScope.launchWhenStarted {
            viewModel.getWeightFlow.collectLatest {
                binding.weightSeekbar.progress = viewModel.calculateSeekbarProgress(it.bmi)
            }
        }

        binding.weightEditBtn.setOnClickListener {
            originWeight = binding.weightTv.text.toString().toInt()

            binding.weightEditView.visibility = View.VISIBLE
            binding.weightTv.visibility = View.GONE
            binding.weightEt.visibility = View.VISIBLE

            binding.weightEditBtn.visibility = View.GONE
            binding.weightOkBtn.visibility = View.VISIBLE
        }

        binding.weightOkBtn.setOnClickListener {
            binding.weightEditView.visibility = View.GONE
            binding.weightTv.visibility = View.VISIBLE
            binding.weightEt.visibility = View.GONE

            binding.weightEditBtn.visibility = View.VISIBLE
            binding.weightOkBtn.visibility = View.GONE

            if (originWeight == 0) {
                viewModel.setWeightUnit(binding.weightEt.text.toString())
                getAccessToken()?.let { viewModel.addWeight(it, date) }
            } else {
                viewModel.setEditWeightUnit(binding.weightEt.text.toString())
                getAccessToken()?.let {  viewModel.editWeight(it, date) }
            }

        }

        binding.backIv.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("date",date)
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        binding.weightInfoIv.setOnClickListener {
            WeightDialogFragment().show(supportFragmentManager, "WeightDialogFragment")
        }
    }

    private fun getAccessToken(): String? {
        val sharedPreferences = getSharedPreferences(TOKEN_SPF_KEY, Context.MODE_PRIVATE)

        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
    }

}