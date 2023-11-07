package com.dangjang.android.presentation.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.dangjang.android.domain.constants.ACCESS_TOKEN_KEY
import com.dangjang.android.domain.constants.TOKEN_SPF_KEY
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.ActivityWeightBinding
import com.dangjang.android.presentation.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class WeightActivity : FragmentActivity() {
    private lateinit var binding: ActivityWeightBinding
    private lateinit var viewModel: HomeViewModel
    private var originWeight: Int = 0
    private var date = ""
    private var startTime: Double = 0.0
    private var endTime: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weight)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_weight)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        binding.vm = viewModel

        binding.lifecycleOwner = this

        date = intent.getStringExtra("date").toString()

        getAccessToken()?.let { viewModel.getWeight(it, date) }

        lifecycleScope.launchWhenStarted {
            viewModel.goToLoginActivityFlow.collectLatest {
                if (it) {
                    val intent = Intent(applicationContext, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

        binding.weightSeekbar.setOnTouchListener({ v, event -> true })
        
        //TODO : lifecyelceScope에 없어도 되는지 테스트 해보기
        lifecycleScope.launchWhenStarted {
            viewModel.getWeightFlow.collectLatest {
                binding.weightSeekbar.progress = viewModel.calculateSeekbarProgress(it.bmi)
                if (it.bmi == 0.0) {
                    binding.weightNoneTitleTv.visibility = View.VISIBLE
                    binding.weightNoneContentTv.visibility = View.VISIBLE
                } else {
                    binding.weightNoneTitleTv.visibility = View.GONE
                    binding.weightNoneContentTv.visibility = View.GONE
                }
            }
        }

        binding.weightEditBtn.setOnClickListener {
            originWeight = binding.weightTv.text.toString().toInt()

            if (originWeight == 0) {
                binding.weightEt.setText("")
            }

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
                if (binding.weightEt.text.toString() != "0" && binding.weightEt.text.toString() != "") {
                    viewModel.setWeightUnit(binding.weightEt.text.toString())
                    getAccessToken()?.let { viewModel.addWeight(it, date) }
                } else {
                    Toast.makeText(applicationContext, "0kg는 입력할 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
            } else {
                if (binding.weightEt.text.toString() != "0" && binding.weightEt.text.toString() != "") {
                    viewModel.setEditWeightUnit(binding.weightEt.text.toString())
                    getAccessToken()?.let {  viewModel.editWeight(it, date) }
                } else {
                    Toast.makeText(applicationContext, "0kg는 입력할 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
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

    override fun onDestroy() {
        super.onDestroy()
        endTime = System.currentTimeMillis().toDouble()
        viewModel.shotWeightExposureLogging(endTime - startTime)
    }

    private fun getAccessToken(): String? {
        val sharedPreferences = getSharedPreferences(TOKEN_SPF_KEY, Context.MODE_PRIVATE)

        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
    }

}