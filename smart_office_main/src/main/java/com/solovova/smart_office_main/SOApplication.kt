package com.solovova.smart_office_main

import android.app.Activity
import android.app.Application
import com.solovova.smart_office_main.service.SensorContainer

class SOApplication : Application() {
    var sensorContainer = SensorContainer(this)
    var mainActivity: Activity? = null
}