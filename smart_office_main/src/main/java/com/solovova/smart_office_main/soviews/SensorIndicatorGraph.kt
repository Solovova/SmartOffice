package com.solovova.smart_office_main.soviews

import android.content.Context
import android.graphics.Color
import android.graphics.DashPathEffect
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.Utils
import com.solovova.smart_office_main.service.SensorIndicator
import com.solovova.smart_office_main.R
import java.util.ArrayList

class SensorIndicatorGraph(context: Context) : ConstraintLayout(context) {
    private var sensorIndicator: SensorIndicator? = null
    private var chart: LineChart

    private fun initChart() {
        run {
            // background color
            chart.setBackgroundColor(Color.WHITE)

            // disable description text
            chart.getDescription().setEnabled(false)

            // enable touch gestures
            chart.setTouchEnabled(true)

            // set listeners
            //chart.setOnChartValueSelectedListener(this)
            chart.setDrawGridBackground(false)

            // create marker to display box when values are selected
            //val mv = MyMarkerView(this, R.layout.custom_marker_view)

            // Set the marker to the chart
            //mv.setChartView(chart)
            //chart.setMarker(mv)

            // enable scaling and dragging
            chart.setDragEnabled(true)
            chart.setScaleEnabled(true)
            // chart.setScaleXEnabled(true);
            // chart.setScaleYEnabled(true);

            // force pinch zoom along both axis
            chart.setPinchZoom(true)
        }

        val xAxis: XAxis
        run {
            // // X-Axis Style // //
            xAxis = chart.getXAxis()

            // vertical grid lines
            xAxis.enableGridDashedLine(10f, 10f, 0f)
        }

        val yAxis: YAxis
        run {
            // // Y-Axis Style // //
            yAxis = chart.getAxisLeft()

            // disable dual axis (only use LEFT axis)
            chart.getAxisRight().setEnabled(false)

            // horizontal grid lines
            yAxis.enableGridDashedLine(10f, 10f, 0f)

            // axis range
            yAxis.axisMaximum = 200f
            yAxis.axisMinimum = -50f
        }


        run {
            // // Create Limit Lines // //
            val llXAxis = LimitLine(9f, "Index 10")
            llXAxis.lineWidth = 4f
            llXAxis.enableDashedLine(10f, 10f, 0f)
            llXAxis.labelPosition = LimitLine.LimitLabelPosition.RIGHT_BOTTOM
            llXAxis.textSize = 10f
            //llXAxis.typeface = tfRegular

            val ll1 = LimitLine(150f, "Upper Limit")
            ll1.lineWidth = 4f
            ll1.enableDashedLine(10f, 10f, 0f)
            ll1.labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP
            ll1.textSize = 10f
            //ll1.typeface = tfRegular

            val ll2 = LimitLine(-30f, "Lower Limit")
            ll2.lineWidth = 4f
            ll2.enableDashedLine(10f, 10f, 0f)
            ll2.labelPosition = LimitLine.LimitLabelPosition.RIGHT_BOTTOM
            ll2.textSize = 10f
            //ll2.typeface = tfRegular

            // draw limit lines behind data instead of on top
            yAxis.setDrawLimitLinesBehindData(true)
            xAxis.setDrawLimitLinesBehindData(true)

            // add limit lines
            yAxis.addLimitLine(ll1)
            yAxis.addLimitLine(ll2)
            //xAxis.addLimitLine(llXAxis);
        }

        setData(45, 180f)

        // draw points over time
        chart.animateX(1500)

        // get the legend (only possible after setting data)
        val l = chart.legend

        // draw legend entries as lines
        l.form = Legend.LegendForm.LINE

        chart.invalidate()
    }

    init {
        inflate(context, R.layout.soview_sensor_indicator_graph, this)
        chart = findViewById(R.id.chart1)
        initChart()
    }

    fun refreshValue() {
        val sensorIndicator = this.sensorIndicator
        if (sensorIndicator != null) {

        }
    }

    private fun refreshAll() {
        val sensorIndicator = this.sensorIndicator
        if (sensorIndicator != null) {
            val dataIndicatorTypeDef =  sensorIndicator.sensor.sensorContainer.getDataIndicatorTypeDef(sensorIndicator.typeEnum)

            refreshValue()
        }
    }

    fun setSensorIndicator(_sensorIndicator: SensorIndicator) {
        if (this.sensorIndicator != _sensorIndicator) {
            this.sensorIndicator = _sensorIndicator

            this.refreshAll()
        }
    }

    private fun setData(count: Int, range: Float) {

        val values = ArrayList<Entry>()

        for (i in 0 until count) {

            val val0 = (Math.random() * range).toFloat() - 30
            values.add(Entry(i.toFloat(), val0, ContextCompat.getDrawable(this.context, R.drawable.star)))
        }

        val set1: LineDataSet

        if (chart.data != null && chart.data.dataSetCount > 0) {
            set1 = chart.data.getDataSetByIndex(0) as LineDataSet
            set1.values = values
            set1.notifyDataSetChanged()
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            // create a dataset and give it a type
            set1 = LineDataSet(values, "DataSet 1")

            set1.setDrawIcons(false)

            // draw dashed line
            set1.enableDashedLine(10f, 5f, 0f)

            // black lines and points
            set1.color = Color.BLACK
            set1.setCircleColor(Color.BLACK)

            // line thickness and point size
            set1.lineWidth = 1f
            set1.circleRadius = 3f

            // draw points as solid circles
            set1.setDrawCircleHole(false)

            // customize legend entry
            set1.formLineWidth = 1f
            set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
            set1.formSize = 15f

            // text size of values
            set1.valueTextSize = 9f

            // draw selection line as dashed
            set1.enableDashedHighlightLine(10f, 5f, 0f)

            // set the filled area
            set1.setDrawFilled(true)
            set1.fillFormatter = IFillFormatter { dataSet, dataProvider -> chart.axisLeft.axisMinimum }

            // set color of filled area
            if (Utils.getSDKInt() >= 18) {
                // drawables only supported on api level 18 and above
                val drawable = ContextCompat.getDrawable(this.context, R.drawable.fade_red)
                set1.fillDrawable = drawable
            } else {
                set1.fillColor = Color.BLACK
            }

            val dataSets = ArrayList<ILineDataSet>()
            dataSets.add(set1) // add the data sets

            // create a data object with the data sets
            val data = LineData(dataSets)

            // set data
            chart.data = data
        }
    }


}