package com.solovova.smart_office_main.service

import android.os.SystemClock
import android.widget.LinearLayout
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionState
import com.solovova.smart_office_main.dataclass.SensorIndicatorDataRecord
import com.solovova.smart_office_main.dataclass.SensorIndicatorTypeEnum
import com.solovova.smart_office_main.SOApplication
import com.solovova.smart_office_main.service.defs.Def
import com.solovova.smart_office_main.service.defs.SensorIndicatorDef
import com.solovova.smart_office_main.soviews.SensorButton
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import android.util.Log
import com.microsoft.signalr.HubConnectionBuilder
import org.json.JSONArray
import org.json.JSONObject

class SensorContainer {
    private var myThread: Thread? = null
    var sensors = mutableMapOf<String, Sensor>()
    private var sensorIndicatorDef = mutableMapOf<SensorIndicatorTypeEnum, SensorIndicatorDef>()
    private var viewContainer : LinearLayout? = null
    var testModeTestData:String
    var app: SOApplication


    //hubConnection
    var hubConnection: HubConnection? = null

    constructor(_app: SOApplication) {
        this.app = _app
        this.sensorIndicatorDef = Def.getDef()
        this.testModeTestData = "on"


        //hub connection
        hubConnection = HubConnectionBuilder.create("http://10.0.2.2:5000/movehub").build()
        val mHubConnection = this.hubConnection
        if (mHubConnection != null) {

            hubConnection?.on("SensorIndicatorChangeValueToApp", { strJSON: String ->
                try {
                    Log.i("RECEIVE", strJSON)
                    val sensorIndicatorDataRecord = SensorIndicatorDataRecord(JSONObject(strJSON))
                    app.mainActivity?.runOnUiThread { this.eventDataIn(sensorIndicatorDataRecord) }
                } catch (e: Exception) {
                }

            }, String::class.java)


            hubConnection?.on("AnswerStartSensorDataToApp", { strJSONArray: String ->
                try {
                    Log.i("AnswerStartSensorData", strJSONArray)
                    val jArray = JSONArray(strJSONArray)
                    val sensor = sensors[jArray.getString(0)]
                    if (sensor != null) {
                        sensor.deleteIndicators()
                        val testSensorIndicator = mutableListOf<SensorIndicatorTypeEnum>()
                        for (ind in 1 until jArray.length()) {
                            testSensorIndicator.add(SensorIndicatorTypeEnum.values()[jArray.getJSONObject(ind).getInt("indicatorTypeEnum")])
                        }
                        sensor.testGenerateData(testSensorIndicator)
                        sensor.createSensorIndicatorButton()
                    }
                } catch (e: Exception) {
                }

            }, String::class.java)
        }

        //end hub connection

        this.myThread = Thread(
            Runnable {
                while (true) {
                    val hubConnection = this.hubConnection
                    if (hubConnection != null) {
                        if (hubConnection.connectionState === HubConnectionState.DISCONNECTED) {
                            Log.i("RECEIVE", "DISCONNECTED")
                        }
                        if (hubConnection.connectionState === HubConnectionState.CONNECTED) {
                            Log.i("RECEIVE", "CONNECTED")
                        }
                    }
                    SystemClock.sleep(1000)
                }
            }
        )
        this.myThread?.start()
    }

    fun setViewContainer (viewContainer: LinearLayout) {
        this.viewContainer = viewContainer
        this.createSensorButtons()
    }

    private fun createSensorButtons(){
        val viewContainer = this.viewContainer
        if (viewContainer != null) {
            if (viewContainer.childCount > 0) viewContainer.removeAllViews()
            val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            for (id in  sensors.keys) {
                val sensor: Sensor? = sensors[id]
                if (sensor != null){
                    val newButton = SensorButton(viewContainer.context)
                    params.setMargins(0, 0, 0, 0)
                    newButton.layoutParams = params
                    viewContainer.addView(newButton)
                    sensor.setLinkToSensorButton(newButton)
                }
            }
            viewContainer.invalidate()
        }
    }

    fun getDataIndicatorTypeDef(_typeEnum: SensorIndicatorTypeEnum): SensorIndicatorDef {
        val dataIndicatorTypeDef = sensorIndicatorDef[_typeEnum]
        dataIndicatorTypeDef ?: return SensorIndicatorDef()
        return dataIndicatorTypeDef
    }



    private fun eventDataIn(sensorIndicatorDataRecord: SensorIndicatorDataRecord) {
        val sensor = this.sensors[sensorIndicatorDataRecord.sensorId]
        sensor?.eventDataIn(sensorIndicatorDataRecord)
    }

    fun onChangeSensor(){

    }

    fun deleteSensor(sensor: Sensor) {
        sensors.remove(sensor.sensorID)
        this.createSensorButtons()
    }

    fun addSensor(_id:String) : String {
        if (sensors[_id] != null) return "Already exists"
        val sensor = Sensor(this, _id )
        sensor.setName(_id)

        //ToDo take data from testData
        if (this.testModeTestData.compareTo("on") == 0) {
            val testSensorIndicator = mutableListOf(
                SensorIndicatorTypeEnum.Temperature ,
                SensorIndicatorTypeEnum.Humidity ,
                SensorIndicatorTypeEnum.Brightness
            )
            sensor.testGenerateData(testSensorIndicator)
        }

        sensors[_id] = sensor
        this.createSensorButtons()
        return "OK"
    }

    private fun sendRequestStartSensorData(sensor: Sensor): Boolean {
        val hubConnection = this.hubConnection
        if (hubConnection != null) {
            if (hubConnection.connectionState === HubConnectionState.CONNECTED) {
                hubConnection.send("RequestStartSensorDataFromApp",sensor.sensorID)
                return true
            }
        }
        return false
    }

    fun setLinkToViewNull(){
        for (sensor in sensors.values) {
            sensor.setLinkToViewNull()
        }
    }

    //Test
    private fun initFromJSON(jObject: JSONObject) {
        var jsonSensors = jObject.getJSONArray("sensors")
        for(ind in 0 until jsonSensors.length() ) {
            var jsonSensor = jsonSensors.getJSONObject(ind)
            val sensorID = jsonSensor.getString("sensorID")
            val sensor = Sensor(this, sensorID )
            sensor.initFromJSON(jsonSensor)
            sensors[sensorID] = sensor
        }
    }

    fun loadFromTestData() {
        try {
            val am = app.assets
            if (am != null) {
                val inputStream: InputStream = am.open("raw/test_data.json")
                val inputStreamReader = InputStreamReader(inputStream)
                val sb = StringBuilder()
                var line: String?
                val br = BufferedReader(inputStreamReader)
                line = br.readLine()
                while (line != null) {
                    sb.append(line)
                    line = br.readLine()
                }
                br.close()
                val jObjectTest = JSONObject(sb.toString())
                this.initFromJSON(jObjectTest)
            }
        } catch (e: Exception) {
            Log.i("READ", e.toString())
        }
    }
}