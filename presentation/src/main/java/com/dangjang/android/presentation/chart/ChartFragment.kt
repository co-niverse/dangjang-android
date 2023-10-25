package com.dangjang.android.presentation.chart

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.dangjang.android.common_ui.BaseFragment
import com.dangjang.android.domain.constants.ACCESS_TOKEN_KEY
import com.dangjang.android.domain.constants.TOKEN_SPF_KEY
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.FragmentChartBinding
import com.dangjang.android.presentation.login.LoginActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChartFragment : BaseFragment<FragmentChartBinding>(R.layout.fragment_chart) {

    private val viewModel by viewModels<ChartViewModel>()
    private var startTime: Double = 0.0
    private var endTime: Double = 0.0

    override fun initView() {
        bind {
            vm = viewModel
        }
    }
    override fun onStart() {
        super.onStart()
        startTime = System.currentTimeMillis().toDouble()

        binding.lifecycleOwner = this

        viewModel.setStartAndEndDate()

        getAccessToken()?.let { viewModel.getChart(it) }

        initBarChart(binding.glucoseChart)
        initLineChart(binding.weightChart)
        initLineChart(binding.stepChart)
        initLineChart(binding.exerciseChart)

        lifecycleScope.launchWhenStarted {
            viewModel.goToLoginActivityFlow.collectLatest {
                if (it) {
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.getChartFlow.collectLatest {
                if (it.bloodSugars.isEmpty()) {
                    binding.glucoseChartNoneTv.visibility = View.VISIBLE
                } else {
                    binding.glucoseChartNoneTv.visibility = View.GONE
                }
                setGlucoseChartData(binding.glucoseChart)

                if (it.weights.isEmpty()) {
                    binding.weightChartNoneTv.visibility = View.VISIBLE
                } else {
                    binding.weightChartNoneTv.visibility = View.GONE
                }
                setWeightChartData(binding.weightChart)

                if (it.stepCounts.isEmpty()) {
                    binding.stepChartNoneTv.visibility = View.VISIBLE
                } else {
                    binding.stepChartNoneTv.visibility = View.GONE
                }
                setStepChartData(binding.stepChart)

                if (it.exerciseCalories.isEmpty()) {
                    binding.exerciseChartNoneTv.visibility = View.VISIBLE
                } else {
                    binding.exerciseChartNoneTv.visibility = View.GONE
                }
                setExerciseChartData(binding.exerciseChart)
            }
        }

        binding.chartAddIv.setOnClickListener {
            viewModel.addStartAndEndDate()
            getAccessToken()?.let { viewModel.getChart(it) }
            lifecycleScope.launch {
                viewModel.getChartFlow.collectLatest {
                    initBarChart(binding.glucoseChart)
                    initLineChart(binding.weightChart)
                    initLineChart(binding.stepChart)
                    initLineChart(binding.exerciseChart)
                }
            }
        }

        binding.chartSubtractIv.setOnClickListener {
            viewModel.subtractStartAndEndDate()
            getAccessToken()?.let { viewModel.getChart(it) }
            lifecycleScope.launch {
                viewModel.getChartFlow.collectLatest {
                    initBarChart(binding.glucoseChart)
                    initLineChart(binding.weightChart)
                    initLineChart(binding.stepChart)
                    initLineChart(binding.exerciseChart)
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.shotChartExposureLogging(endTime - startTime)
    }

    private fun initBarChart(barChart: BarChart) {
        barChart.setDrawGridBackground(false)
        barChart.setDrawBarShadow(false)
        barChart.setDrawBorders(false)

        val description = Description()
        description.isEnabled = false
        barChart.description = description

        barChart.setTouchEnabled(false)
        barChart.legend.isEnabled = false

        barChart.animateY(1000)
        barChart.animateX(1000)

        val xAxis: XAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.textColor = Color.BLACK
        xAxis.setDrawAxisLine(false)
        xAxis.setDrawGridLines(false)

        xAxis.valueFormatter = IndexAxisValueFormatter(viewModel.getDateList())

        val leftAxis: YAxis = barChart.axisLeft
        leftAxis.setDrawAxisLine(false)
        leftAxis.setDrawGridLines(false)
        leftAxis.textColor = Color.BLACK

        val rightAxis: YAxis = barChart.axisRight
        rightAxis.setDrawAxisLine(false)
        rightAxis.setDrawGridLines(false)
        rightAxis.setDrawLabels(false)
    }

    // 차트 데이터 설정
    private fun setGlucoseChartData(barChart: BarChart) {
        barChart.setScaleEnabled(false)
        val title = "혈당"

        //최소값
        val minGlucoseDataSet = BarDataSet(viewModel.getGlucoseMinList(), title)
        minGlucoseDataSet.setColors(
            ContextCompat.getColor(requireContext(), R.color.dark_green)
        )

        //최대값
        val maxGlucoseDataSet = BarDataSet(viewModel.getGlucoseMaxList(), title)
        maxGlucoseDataSet.setColors(
            ContextCompat.getColor(requireContext(), R.color.green)
        )

        // 차트에 적용
        val barData = BarData()
        barData.addDataSet(maxGlucoseDataSet)
        barData.addDataSet(minGlucoseDataSet)
        barData.barWidth = 0.5f
        barChart.data = barData
        barChart.invalidate()
    }

    // line 차트 설정
    private fun initLineChart(lineChart: LineChart) {
        lineChart.setDrawGridBackground(false)
        lineChart.setDrawBorders(false)

        val description = Description()
        description.isEnabled = false
        lineChart.description = description

        lineChart.setTouchEnabled(false)
        lineChart.legend.isEnabled = false

        val xAxis: XAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.textColor = Color.BLACK
        xAxis.setDrawAxisLine(false)
        xAxis.setDrawGridLines(false)
        xAxis.valueFormatter = IndexAxisValueFormatter(viewModel.getDateList())

        val leftAxis: YAxis = lineChart.axisLeft
        leftAxis.setDrawAxisLine(false)
        leftAxis.setDrawGridLines(false)
        leftAxis.spaceTop = 30f
        leftAxis.spaceBottom = 30f
        leftAxis.textColor = Color.BLACK

        val rightAxis: YAxis = lineChart.axisRight
        rightAxis.setDrawAxisLine(false)
        rightAxis.setDrawGridLines(false)
        rightAxis.setDrawLabels(false)
    }

    // line 차트 데이터 설정
    private fun setWeightChartData(weightChart: LineChart) {
        val title = "체중"
        weightChart.setScaleEnabled(false)
        val weightDataSet = LineDataSet(viewModel.getWeightList(), title)

        weightDataSet.setColors(
            ContextCompat.getColor(requireContext(), R.color.green)
        )
        weightDataSet.lineWidth = 3f
        weightDataSet.circleColors = listOf(
            ContextCompat.getColor(requireContext(), R.color.green)
        )
        weightDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        weightDataSet.cubicIntensity = 0.2f

        val weightData = LineData()
        weightData.addDataSet(weightDataSet)

        weightChart.data = weightData
        weightChart.invalidate()
    }

    private fun setStepChartData(stepChart: LineChart) {
        val title = "걸음수"
        stepChart.setScaleEnabled(false)
        val stepDataSet = LineDataSet(viewModel.getStepList(), title)

        stepDataSet.setColors(
            ContextCompat.getColor(requireContext(), R.color.green)
        )
        stepDataSet.lineWidth = 3f
        stepDataSet.circleColors = listOf(
            ContextCompat.getColor(requireContext(), R.color.green)
        )
        stepDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        stepDataSet.cubicIntensity = 0.2f

        val stepData = LineData()
        stepData.addDataSet(stepDataSet)

        stepChart.data = stepData
        stepChart.invalidate()
    }

    private fun setExerciseChartData(exerciseChart: LineChart) {
        val title = "운동 소모 칼로리"
        exerciseChart.setScaleEnabled(false)
        val exerciseDataSet = LineDataSet(viewModel.getExerciseList(), title)

        exerciseDataSet.setColors(
            ContextCompat.getColor(requireContext(), R.color.green)
        )
        exerciseDataSet.lineWidth = 3f
        exerciseDataSet.circleColors = listOf(
            ContextCompat.getColor(requireContext(), R.color.green)
        )
        exerciseDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        exerciseDataSet.cubicIntensity = 0.2f

        val exerciseData = LineData()
        exerciseData.addDataSet(exerciseDataSet)

        exerciseChart.data = exerciseData
        exerciseChart.invalidate()
    }

    private fun getAccessToken(): String? {
        val sharedPreferences = requireContext().getSharedPreferences(TOKEN_SPF_KEY, Context.MODE_PRIVATE)
        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
    }
}