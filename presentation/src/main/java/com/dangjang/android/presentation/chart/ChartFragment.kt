package com.dangjang.android.presentation.chart

import android.content.Context
import android.graphics.Color
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.dangjang.android.common_ui.BaseFragment
import com.dangjang.android.domain.constants.ACCESS_TOKEN_KEY
import com.dangjang.android.domain.constants.TOKEN_SPF_KEY
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.FragmentChartBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ChartFragment : BaseFragment<FragmentChartBinding>(R.layout.fragment_chart) {

    private val viewModel by viewModels<ChartViewModel>()

    override fun initView() {
        bind {
            vm = viewModel
        }
    }
    override fun onStart() {
        super.onStart()
        binding.lifecycleOwner = this

        getAccessToken()?.let { viewModel.getChart(it) }

        lifecycleScope.launchWhenStarted {
            viewModel.getChartFlow.collectLatest {
                Log.e("건강차트 조회 test", it.toString())
            }
        }

        viewModel.setStartAndEndDate()

        binding.chartAddIv.setOnClickListener {
            viewModel.addStartAndEndDate()
        }

        binding.chartSubtractIv.setOnClickListener {
            viewModel.subtractStartAndEndDate()
        }

        initBarChart(binding.glucoseChart)
        setData(binding.glucoseChart)

        initLineChart(binding.weightChart)
        setLineChartData(binding.weightChart)

        initLineChart(binding.stepChart)
        setLineChartData(binding.stepChart)

        initLineChart(binding.exerciseChart)
        setLineChartData(binding.exerciseChart)
    }

    // 바 차트 설정
    private fun initBarChart(barChart: BarChart) {
        // 차트 회색 배경 설정 (default = false)
        barChart.setDrawGridBackground(false)
        // 막대 그림자 설정 (default = false)
        barChart.setDrawBarShadow(false)
        // 차트 테두리 설정 (default = false)
        barChart.setDrawBorders(false)

        val description = Description()
        // 오른쪽 하단 모서리 설명 레이블 텍스트 표시 (default = false)
        description.isEnabled = false
        barChart.description = description

        // 터치 유무
        barChart.setTouchEnabled(false)

        // 차트 범례
        barChart.legend.isEnabled = false

        // X, Y 바의 애니메이션 효과
        barChart.animateY(1000)
        barChart.animateX(1000)

        // 바텀 좌표 값
        val xAxis: XAxis = barChart.xAxis
        // x축 위치 설정
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        // 그리드 선 수평 거리 설정
        xAxis.granularity = 1f
        // x축 텍스트 컬러 설정
        xAxis.textColor = Color.BLACK
        // x축 선 설정 (default = true)
        xAxis.setDrawAxisLine(false)
        // 격자선 설정 (default = true)
        xAxis.setDrawGridLines(false)
        // 라벨 포맷 설정
        xAxis.valueFormatter = LabelCustomFormatter()

        val leftAxis: YAxis = barChart.axisLeft
        // 좌측 선 설정 (default = true)
        leftAxis.setDrawAxisLine(false)
        leftAxis.setDrawGridLines(false)
        // 좌측 텍스트 컬러 설정
        leftAxis.textColor = Color.BLACK

        val rightAxis: YAxis = barChart.axisRight
        // 우측 선 설정 (default = true)
        rightAxis.setDrawAxisLine(false)
        rightAxis.setDrawGridLines(false)
        rightAxis.setDrawLabels(false)

    }

    // 차트 데이터 설정
    private fun setData(barChart: BarChart) {
        // Zoom In / Out 가능 여부 설정
        barChart.setScaleEnabled(false)

        val valueList = ArrayList<BarEntry>()
        val title = "혈당"
        // 임의 데이터
        for (i in 0 until 7) {
            valueList.add(BarEntry(i.toFloat(), i * 100f + 10f))
        }

        val barDataSet = BarDataSet(valueList, title)
        // 바 색상 설정 (ColorTemplate.LIBERTY_COLORS)
        barDataSet.setColors(
            ContextCompat.getColor(requireContext(), R.color.green)
        )

        val valueList2 = ArrayList<BarEntry>()
        // 임의 데이터
        for (i in 0 until 7) {
            valueList2.add(BarEntry(i.toFloat(), i * 50f + 20f))
        }

        val barDataSet2 = BarDataSet(valueList2, title)
        barDataSet2.barBorderWidth = 2f
        barDataSet2.barBorderColor = Color.WHITE
        barDataSet2.color = Color.WHITE

        val barData = BarData()
        barData.addDataSet(barDataSet)
        barData.addDataSet(barDataSet2)
        barData.barWidth = 0.5f

        barChart.data = barData
        barChart.invalidate()
    }

    class LabelCustomFormatter : ValueFormatter() {
        private var index = 0

        override fun getFormattedValue(value: Float): String {
            index = value.toInt()
            return when (index) {
                0 -> "월"
                1 -> "화"
                2 -> "수"
                3 -> "목"
                4 -> "금"
                5 -> "토"
                6 -> "일"
                else -> ""
            }
        }

        override fun getBarStackedLabel(value: Float, stackedEntry: BarEntry?): String {
            return super.getBarStackedLabel(value, stackedEntry)
        }
    }

    // line 차트 설정
    private fun initLineChart(lineChart: LineChart) {
        // 차트 회색 배경 설정 (default = false)
        lineChart.setDrawGridBackground(false)
        // 차트 테두리 설정 (default = false)
        lineChart.setDrawBorders(false)

        val description = Description()
        // 오른쪽 하단 모서리 설명 레이블 텍스트 표시 (default = false)
        description.isEnabled = false
        lineChart.description = description

        // 터치 유무
        lineChart.setTouchEnabled(false)

        // 차트 범례
        lineChart.legend.isEnabled = false

        // X, Y 바의 애니메이션 효과
        lineChart.animateY(1000)
        lineChart.animateX(1000)

        // 바텀 좌표 값
        val xAxis: XAxis = lineChart.xAxis
        // x축 위치 설정
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        // 그리드 선 수평 거리 설정
        xAxis.granularity = 1f
        // x축 텍스트 컬러 설정
        xAxis.textColor = Color.BLACK
        // x축 선 설정 (default = true)
        xAxis.setDrawAxisLine(false)
        // 격자선 설정 (default = true)
        xAxis.setDrawGridLines(false)
        // 라벨 포맷 설정
        xAxis.valueFormatter = LabelCustomFormatter()

        val leftAxis: YAxis = lineChart.axisLeft
        // 좌측 선 설정 (default = true)
        leftAxis.setDrawAxisLine(false)
        leftAxis.setDrawGridLines(false)
        leftAxis.spaceTop = 30f
        leftAxis.spaceBottom = 30f
        // 좌측 텍스트 컬러 설정
        leftAxis.textColor = Color.BLACK

        val rightAxis: YAxis = lineChart.axisRight
        // 우측 선 설정 (default = true)
        rightAxis.setDrawAxisLine(false)
        rightAxis.setDrawGridLines(false)
        rightAxis.setDrawLabels(false)
    }

    // line 차트 데이터 설정
    private fun setLineChartData(lineChart: LineChart) {
        // Zoom In / Out 가능 여부 설정
        lineChart.setScaleEnabled(false)

        val valueList = ArrayList<Entry>()
        val title = "체중"
        // 임의 데이터
            valueList.add(Entry(0f, 61.5f))
            valueList.add(Entry(1f, 60f))
            valueList.add(Entry(2f, 60.5f))
            valueList.add(Entry(3f, 60f))
            valueList.add(Entry(4f, 61f))
            valueList.add(Entry(5f, 61.2f))
            valueList.add(Entry(6f, 61.5f))

        val lineDataSet = LineDataSet(valueList, title)
        // 바 색상 설정 (ColorTemplate.LIBERTY_COLORS)
        lineDataSet.setColors(
            ContextCompat.getColor(requireContext(), R.color.green)
        )
        lineDataSet.lineWidth = 3f
        lineDataSet.circleColors = listOf(
            ContextCompat.getColor(requireContext(), R.color.green)
        )
        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        lineDataSet.cubicIntensity = 0.2f

        val lineData = LineData()
        lineData.addDataSet(lineDataSet)

        lineChart.data = lineData
        lineChart.invalidate()
    }

    private fun getAccessToken(): String? {
        val sharedPreferences = requireContext().getSharedPreferences(TOKEN_SPF_KEY, Context.MODE_PRIVATE)
        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
    }
}