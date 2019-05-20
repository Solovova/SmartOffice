package com.solovova.smart_office_main.service

import android.widget.LinearLayout
import com.solovova.smart_office_main.dataclass.SensorIndicatorTypeEnum
import com.solovova.smart_office_main.fragments.FragmentSensor
import com.solovova.smart_office_main.soviews.SensorButton
import com.solovova.smart_office_main.soviews.SensorIndicatorButton
import com.solovova.smart_office_main.dataclass.SensorIndicatorDataRecord
import org.json.JSONObject

//All good
class Sensor(_sensorContainer: SensorContainer, _sensorID: String) {
    private var indicators = mutableListOf<SensorIndicator>()
    var sensorID: String = _sensorID
    var sensorName: String = ""
    val sensorContainer: SensorContainer = _sensorContainer
    private var mSensorButton: SensorButton? = null
    private var mFragmentSensor: FragmentSensor? = null
    private var mSensorIndicatorContainer: LinearLayout? = null

    fun setName(sensorName:String){
        this.sensorName = sensorName
    }

    fun setLinkToSensorButton(sensorButton: SensorButton){
        this.mSensorButton = sensorButton
        sensorButton.setSensor(this)
    }

    fun setLinkToView(fragmentSensor: FragmentSensor, sensorIndicatorContainer: LinearLayout){
        if (this.mFragmentSensor != fragmentSensor || this.mSensorIndicatorContainer != sensorIndicatorContainer) {
            this.sensorContainer.setLinkToViewNull()
            this.mFragmentSensor = fragmentSensor
            this.mSensorIndicatorContainer = sensorIndicatorContainer
            this.createSensorIndicatorButton()
        }
    }

    fun setLinkToViewNull(){
        this.mFragmentSensor = null
        this.mSensorIndicatorContainer = null
    }

     fun createSensorIndicatorButton(){
        val sensorIndicatorContainer = this.mSensorIndicatorContainer
        if (sensorIndicatorContainer != null) {
            if (sensorIndicatorContainer.childCount > 0) sensorIndicatorContainer.removeAllViews()

            for (indicator in indicators) {
                val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
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

    fun getAlarmState(_type: SensorIndicatorTypeEnum? = null): Int {
        var maxAlarm  = 0
        for (indicator in indicators){
            if (indicator.typeEnum == _type || _type == null ) {
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
        mSensorButton?.refreshValue()
        mFragmentSensor?.refreshHead()
        sensorContainer.onChangeSensor()
    }

    fun deleteIndicators() {
        this.indicators =  mutableListOf<SensorIndicator>()
    }

    fun initFromJSON(jObject: JSONObject) {
        this.setName(jObject.getString("sensorName"))
        var jsonSensorIndicators = jObject.getJSONArray("indicators")
        for(ind in 0 until jsonSensorIndicators.length() ) {
            var jsonSensorIndicator = jsonSensorIndicators.getJSONObject(ind)
            val type = SensorIndicatorTypeEnum.values()[jsonSensorIndicator.getInt("type")]
            val sensorIndicator = SensorIndicator(this, type )
            sensorIndicator.initFromJSON(jsonSensorIndicator)
            indicators.add(sensorIndicator)
        }
    }

    fun setLinkToGraphNull() {
        for (indicator in indicators) {
            indicator.setLinkToGraphNull()
        }
    }

    //Test functions
    //call if we work without SignalR
    fun testGenerateData(testSensorIndicatorTypeEnum : List<SensorIndicatorTypeEnum>) {
        var sensorIndicator: SensorIndicator
        for (ind in 0 until testSensorIndicatorTypeEnum.size) {
            sensorIndicator = SensorIndicator(this, testSensorIndicatorTypeEnum[ind])
            sensorIndicator.testGenerateData()
            indicators.add(sensorIndicator)
        }
    }

}