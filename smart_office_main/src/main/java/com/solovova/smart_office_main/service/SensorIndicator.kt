package com.solovova.smart_office_main.service

import android.os.SystemClock
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.solovova.smart_office_main.dataclass.SensorIndicatorDataRecord
import com.solovova.smart_office_main.dataclass.SensorIndicatorTypeEnum
import com.solovova.smart_office_main.service.defs.SensorIndicatorDef
import com.solovova.smart_office_main.soviews.SensorIndicatorButton
import com.solovova.smart_office_main.soviews.SensorIndicatorGraph
import org.json.JSONObject

//All good
//ToDo change arrow Up Down in SensorIndicatorButton
class SensorIndicator(val sensor: Sensor, var typeEnum: SensorIndicatorTypeEnum) {
    private var indicatorValue: Double
    private var indicatorOldValue: Double
    private var indicatorValueTime: Long
    var sensorIndicatorDef: SensorIndicatorDef = sensor.sensorContainer.getDataIndicatorTypeDef(this.typeEnum)
    var dataset : MutableList<Double>

    private var alarmBorder: Array<Double>

    private var mSensorIndicatorButton: SensorIndicatorButton? = null
    private var mSensorIndicatorGraph: SensorIndicatorGraph? = null
    private var mSensorIndicatorGraphContainer: ConstraintLayout? = null


    init {
        this.alarmBorder = sensorIndicatorDef.defAlarmBorder.clone()
        this.indicatorValue = sensorIndicatorDef.defValue
        this.indicatorOldValue = 0.0
        this.indicatorValueTime = 0
        this.dataset = mutableListOf()
    }

    fun setLinkToSensorIndicatorButton(sensorIndicatorButton: SensorIndicatorButton) {
        if (this.mSensorIndicatorButton !=sensorIndicatorButton) {
            this.mSensorIndicatorButton = sensorIndicatorButton
            sensorIndicatorButton.setSensorIndicator(this)
        }
    }

    fun getAlarmCode():Int {
        val dataIndicatorTypeDef =  this.sensorIndicatorDef
        when (dataIndicatorTypeDef.defTypeOfAlarm) {
            0 -> {
                if (this.indicatorValue <= this.alarmBorder[0]) return 1
                if (this.indicatorValue >= this.alarmBorder[1]) return 2
                return 0
            }
            1 -> {
                if (this.indicatorValue >= this.alarmBorder[1]) return 2
                if (this.indicatorValue >= this.alarmBorder[0]) return 1
                return 0
            }
            else -> return 0
        }
    }

    fun getIndicatorValue(): Double {
        return this.indicatorValue
    }

    private fun setIndicatorValue(value: Double){
        this.indicatorOldValue = this.indicatorValue
        this.indicatorValueTime = SystemClock.currentThreadTimeMillis()
        this.indicatorValue = value
        this.dataset.add(value)
        mSensorIndicatorButton?.refreshValue()
        mSensorIndicatorGraph?.refreshValue()
        sensor.onChangeSensorIndicator()
    }

    fun eventDataIn(sensorIndicatorDataRecord: SensorIndicatorDataRecord) {
        this.setIndicatorValue(sensorIndicatorDataRecord.indicatorValue)
    }

    fun initFromJSON(jObject: JSONObject) {
        this.indicatorValue = jObject.getDouble("value")
    }

    fun setLinkToViewGraph(sensorIndicatorGraphContainer: ConstraintLayout) {
        if (this.mSensorIndicatorGraphContainer != sensorIndicatorGraphContainer) {
            this.sensor.sensorContainer.setLinkToGraphNull()
            this.mSensorIndicatorGraphContainer = sensorIndicatorGraphContainer
            this.createSensorIndicatorGraph()
        }
    }

    private fun createSensorIndicatorGraph() {
        val sensorIndicatorGraphContainer = this.mSensorIndicatorGraphContainer
        if (sensorIndicatorGraphContainer != null) {
            if (sensorIndicatorGraphContainer.childCount > 0) sensorIndicatorGraphContainer.removeAllViews()
            val params: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )

            val newSensorIndicatorGraph = SensorIndicatorGraph(sensorIndicatorGraphContainer.context)
            params.setMargins(5, 5, 5, 5)
            newSensorIndicatorGraph.layoutParams = params
            sensorIndicatorGraphContainer.addView(newSensorIndicatorGraph)
            this.mSensorIndicatorGraph = newSensorIndicatorGraph
            newSensorIndicatorGraph.setSensorIndicator(this)
            sensorIndicatorGraphContainer.invalidate()
        }
    }

    fun setLinkToGraphNull() {
        this.mSensorIndicatorGraphContainer = null
    }

    //Test functions
    fun testGenerateData() {

    }
}