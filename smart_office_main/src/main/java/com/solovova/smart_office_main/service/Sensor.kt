package com.solovova.smart_office_main.service

import android.widget.LinearLayout
import com.solovova.smart_office_main.dataclass.SensorIndicatorTypeEnum
import com.solovova.smart_office_main.fragments.FragmentSensor
import com.solovova.smart_office_main.soviews.SensorButton
import com.solovova.smart_office_main.soviews.SensorIndicatorButton
import com.solovova.smart_office_main.dataclass.SensorIndicatorDataRecord

class Sensor(_sensorContainer: SensorContainer, _sensorID: String) {
    private var indicators = mutableListOf<SensorIndicator>()
    var sensorID: String = _sensorID
    var sensorName: String = ""
    val sensorContainer: SensorContainer = _sensorContainer
    private var sensorButton: SensorButton? = null
    private var fragmentSensor: FragmentSensor? = null
    private var sensorIndicatorContainer: LinearLayout? = null


    fun setName(_sensorName:String){
        this.sensorName = _sensorName
    }

    fun setLinkToSensorButton(sensorButton: SensorButton){
        this.sensorButton = sensorButton
        sensorButton.setSensor(this)
    }

    fun setLinkToView(_fragmentSensor: FragmentSensor, _sensorIndicatorContainer: LinearLayout){
        if (this.fragmentSensor != _fragmentSensor || this.sensorIndicatorContainer != _sensorIndicatorContainer) {
            this.sensorContainer.setLinkToViewNull()
            this.fragmentSensor = _fragmentSensor
            this.sensorIndicatorContainer = _sensorIndicatorContainer
            this.createSensorIndicatorButton()
        }
    }

    fun setLinkToViewNull(){
        this.fragmentSensor = null
        this.sensorIndicatorContainer = null
    }

     fun createSensorIndicatorButton(){
        val sensorIndicatorContainer = this.sensorIndicatorContainer
        if (sensorIndicatorContainer != null) {
            if (sensorIndicatorContainer.childCount > 0) sensorIndicatorContainer.removeAllViews()
            val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            for (indicator in indicators) {
                val newButtonIndicator =
                    SensorIndicatorButton(sensorIndicatorContainer.context)
                params.setMargins(5, 5, 5, 5)
                newButtonIndicator.layoutParams = params
                sensorIndicatorContainer.addView(newButtonIndicator)
                indicator.setLinkToSensorIndicatorButton(newButtonIndicator)
            }
            sensorIndicatorContainer.invalidate()
        }
    }

    //call if we work without SignalR
    fun testGenerateData(testSensorIndicatorTypeEnum : List<SensorIndicatorTypeEnum>) {
        var sensorIndicator: SensorIndicator
        for (ind in 0 until testSensorIndicatorTypeEnum.size) {
            sensorIndicator = SensorIndicator(testSensorIndicatorTypeEnum[ind], this)
            sensorIndicator.testGenerateData()
            indicators.add(sensorIndicator)
        }
    }

    fun getAlarmState(typeEnum: SensorIndicatorTypeEnum? = null): Int {
        var maxAlarm  = 0
        for (indicator in indicators){
            if (indicator.typeEnum == typeEnum || typeEnum == null ) {
                val tmpAlarm = indicator.getAlarmCode()
                if (tmpAlarm > maxAlarm) maxAlarm = tmpAlarm
            }
        }
        return maxAlarm
    }

    fun eventDataIn(sensorIndicatorDataRecord: SensorIndicatorDataRecord) {
        for (indicator in indicators) {
            if (indicator.typeEnum == sensorIndicatorDataRecord.type) {
                indicator.eventDataIn(sensorIndicatorDataRecord)
                break
            }
        }
    }

    fun onChangeSensorIndicator(){
        sensorButton?.refreshValue()
        fragmentSensor?.refreshHead()
        sensorContainer.onChangeSensor()
    }

    fun deleteIndicators() {
        this.indicators =  mutableListOf<SensorIndicator>()
    }

}