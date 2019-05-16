package com.solovova.smart_office_main.dataclass

import org.json.JSONObject

class SensorIndicatorDataRecord {
    var sensorId: String
    var type: SensorIndicatorTypeEnum
    var indicatorValue: Double
    var time: Long

    constructor (sensorId: String, indicatorTypeEnum: SensorIndicatorTypeEnum, time:Long, indicatorValue: Double) {
        this.sensorId = sensorId
        this.type = indicatorTypeEnum
        this.indicatorValue = indicatorValue
        this.time = time
    }

    constructor(jsObject: JSONObject) {
        this.sensorId = jsObject.getString("sensorId")
        this.type = SensorIndicatorTypeEnum.values()[jsObject.getInt("type")]
        this.indicatorValue = jsObject.getDouble("indicatorValue")
        this.time = jsObject.getLong("time")
    }

    fun getJSObject() : JSONObject {
        val jObject = JSONObject()
        jObject.put("sensorId",this.sensorId)
        jObject.put("type",this.type.ordinal)
        jObject.put("indicatorValue",this.indicatorValue)
        jObject.put("time",this.time)
        return jObject
    }
}