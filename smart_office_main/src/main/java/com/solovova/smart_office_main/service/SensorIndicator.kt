package com.solovova.smart_office_main.service

import android.os.SystemClock
import androidx.constraintlayout.widget.ConstraintLayout
import com.solovova.smart_office_main.dataclass.SensorIndicatorDataRecord
import com.solovova.smart_office_main.dataclass.SensorIndicatorTypeEnum
import com.solovova.smart_office_main.service.defs.SensorIndicatorDef
import com.solovova.smart_office_main.soviews.SensorIndicatorButton
import com.solovova.smart_office_main.soviews.SensorIndicatorGraph
import org.json.JSONObject

class SensorIndicator(_sensor: Sensor, _typeEnum: SensorIndicatorTypeEnum) {
    private var indicatorValue: Double
    private var indicatorOldValue: Double
    private var indicatorValueTime: Long
    var sensorIndicatorDef: SensorIndicatorDef

    var typeEnum: SensorIndicatorTypeEnum = _typeEnum
    val sensor: Sensor = _sensor


    private var alarmBorder: Array<Double>


    private var sensorIndicatorButton: SensorIndicatorButton? = null

    private var sensorIndicatorGraph: SensorIndicatorGraph? = null
    private var sensorIndicatorGraphContainer: ConstraintLayout? = null


    init {
        this.sensorIndicatorDef =  _sensor.sensorContainer.getDataIndicatorTypeDef(this.typeEnum)
        this.alarmBorder = sensorIndicatorDef.defAlarmBorder.clone()
        this.indicatorValue = sensorIndicatorDef.defValue
        this.indicatorOldValue = 0.0
        this.indicatorValueTime = 0
    }

    fun testGenerateData() {

    }

    fun setLinkToSensorIndicatorButton(_sensorIndicatorButton: SensorIndicatorButton) {
        if (this.sensorIndicatorButton !=_sensorIndicatorButton) {
            this.sensorIndicatorButton = _sensorIndicatorButton
            _sensorIndicatorButton.setSensorIndicator(this)
        }
    }

    fun getAlarmCode():Int {
        val dataIndicatorTypeDef =  this.sensor.sensorContainer.getDataIndicatorTypeDef(this.typeEnum)
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

    private fun setIndicatorValue(_value: Double){
        this.indicatorOldValue = this.indicatorValue
        this.indicatorValueTime = SystemClock.currentThreadTimeMillis()
        this.indicatorValue = _value
        sensorIndicatorButton?.refreshValue()
        sensor.onChangeSensorIndicator()
    }

    fun eventDataIn(sensorIndicatorDataRecord: SensorIndicatorDataRecord) {
        this.setIndicatorValue(sensorIndicatorDataRecord.indicatorValue)
    }

    fun initFromJSON(jObject: JSONObject) {
        this.indicatorValue = jObject.getDouble("value")
    }

    fun setLinkToViewGraph(sensorIndicatorGraphContainer: ConstraintLayout) {
        if (this.sensorIndicatorGraphContainer != sensorIndicatorGraphContainer) {
            this.sensorIndicatorGraphContainer = sensorIndicatorGraphContainer
            this.createSensorIndicatorGraph()
        }
    }

    private fun createSensorIndicatorGraph() {
        val sensorIndicatorGraphContainer = this.sensorIndicatorGraphContainer
        if (sensorIndicatorGraphContainer != null) {
            if (sensorIndicatorGraphContainer.childCount > 0) sensorIndicatorGraphContainer.removeAllViews()
            val params: ConstraintLayout.LayoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )

            val newSensorIndicatorGraph = SensorIndicatorGraph(sensorIndicatorGraphContainer.context)
            params.setMargins(5, 5, 5, 5)
            newSensorIndicatorGraph.layoutParams = params
            sensorIndicatorGraphContainer.addView(newSensorIndicatorGraph)
            this.sensorIndicatorGraph = newSensorIndicatorGraph
            sensorIndicatorGraphContainer.invalidate()
        }
    }
}