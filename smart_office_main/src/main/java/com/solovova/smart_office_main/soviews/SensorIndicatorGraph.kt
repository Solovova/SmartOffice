package com.solovova.smart_office_main.soviews

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import com.solovova.smart_office_main.service.SensorIndicator
import com.solovova.smart_office_main.R

class SensorIndicatorGraph(context: Context) : ConstraintLayout(context) {
    private var sensorIndicator: SensorIndicator? = null

    init {
        inflate(context, R.layout.soview_sensor_indicator_graph, this)
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


}