package com.example.smart_office_emulate_sensor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import com.microsoft.signalr.HubConnectionState
import org.json.JSONArray
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    private var btnStart: Button? = null
    private var btnSend: Button? = null
    var hubConnection: HubConnection? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Logic

        this.btnStart = findViewById(R.id.buttonServer)
        this.btnSend = findViewById(R.id.buttonSend)

        hubConnection = HubConnectionBuilder.create("http://10.0.2.2:5000/movehub").build()
//
//        hubConnection?.on("RequestStartSensorDataToSensor", { strID:String->
//            try{
//                Log.i("StartSensorDataToSensor",strID)
//                val jArray = JSONArray()
//                jArray.put(strID)
//                val sensor = (application as SOApplicationEmulator).sensorContainer.sensors[strID]
//                if (sensor != null) {
//                    for (indicator in sensor.indicators) {
//                        val jObject = indicator.getSensorIndicatorDataRecord().getJSObject()
//                        jArray.put(jObject)
//                    }
//                }
//                hubConnection?.send("AnswerStartSensorDataFromSensor",jArray.toString())
//            }catch (e: java.lang.Exception){}
//
//        }, String::class.java)


        val seekBarChangeListener0 = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val teValue = findViewById<EditText>(R.id.teValue)
                teValue.text = Editable.Factory.getInstance().newEditable(progress.toString())

                val teSensorIndicator = findViewById<EditText>(R.id.teSensorIndicator)
                teSensorIndicator.text = Editable.Factory.getInstance().newEditable("0")

                send()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        }

        val tSeekBar0 = findViewById<SeekBar>(R.id.seekBar0)
        tSeekBar0.setOnSeekBarChangeListener(seekBarChangeListener0)

        val seekBarChangeListener1 = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val teValue = findViewById<EditText>(R.id.teValue)
                teValue.text = Editable.Factory.getInstance().newEditable(progress.toString())

                val teSensorIndicator = findViewById<EditText>(R.id.teSensorIndicator)
                teSensorIndicator.text = Editable.Factory.getInstance().newEditable("1")

                send()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        }

        val tSeekBar1 = findViewById<SeekBar>(R.id.seekBar1)
        tSeekBar1.setOnSeekBarChangeListener(seekBarChangeListener1)

        val seekBarChangeListener2 = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val teValue = findViewById<EditText>(R.id.teValue)
                teValue.text = Editable.Factory.getInstance().newEditable(progress.toString())

                val teSensorIndicator = findViewById<EditText>(R.id.teSensorIndicator)
                teSensorIndicator.text = Editable.Factory.getInstance().newEditable("2")

                send()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        }
        //Test
        val tSeekBar2 = findViewById<SeekBar>(R.id.seekBar2)
        tSeekBar2.setOnSeekBarChangeListener(seekBarChangeListener2)

        val seekBarChangeListener3 = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val teValue = findViewById<EditText>(R.id.teValue)
                teValue.text = Editable.Factory.getInstance().newEditable(progress.toString())

                val teSensorIndicator = findViewById<EditText>(R.id.teSensorIndicator)
                teSensorIndicator.text = Editable.Factory.getInstance().newEditable("3")

                send()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        }

        val tSeekBar3 = findViewById<SeekBar>(R.id.seekBar3)
        tSeekBar3.setOnSeekBarChangeListener(seekBarChangeListener3)

        val onClickListenerStart = View.OnClickListener {
            val hubConnection = this.hubConnection
            val btnStart = this.btnStart
            if (hubConnection != null && btnStart != null) {
                if (btnStart.text.toString().toLowerCase().compareTo("start") == 0) {
                    if (hubConnection.connectionState === HubConnectionState.DISCONNECTED) {
                        hubConnection.start()
                    }
                    btnStart.text = "stop"
                } else if (btnStart.text.toString().toLowerCase().compareTo("stop") == 0) {
                    if (hubConnection.connectionState === HubConnectionState.CONNECTED) {
                        hubConnection.stop()
                    }
                    btnStart.text="start"

                }
            }
        }
        btnStart?.setOnClickListener(onClickListenerStart)


        val onClickListenerSend = View.OnClickListener {
            this.send()
        }
        btnSend?.setOnClickListener(onClickListenerSend)


    }

    // Server -> Android <sensorIndicatorDataRecord> set Android SensorIndicator
    // Android -> Server request String: ID  request for get start data for Sensor
    //      Server -> Android answer [sensorIndicatorDataRecord,..] array of start data for each SensorIndicator
    //               by this answer Android add or delete SensorIndicator in Sensor

    private fun send() {
        val hubConnection = this.hubConnection
        if (hubConnection != null) {
            if (hubConnection.connectionState == HubConnectionState.CONNECTED) {
                try {
                    val teSensorID = findViewById<EditText>(R.id.teSensorID)
                    val teSensorIndicator = findViewById<EditText>(R.id.teSensorIndicator)
                    val teValue = findViewById<EditText>(R.id.teValue)

                    val jObject = JSONObject()
                    jObject.put("sensorId",teSensorID.text.toString())
                    jObject.put("type",teSensorIndicator.text.toString().toInt())
                    jObject.put("indicatorValue",teValue.text.toString().toDouble())
                    jObject.put("time",0)
                    hubConnection.send("SensorIndicatorChangeValueFromSensor",jObject.toString())
                }catch (e: Exception) {}
            }
        }
    }
}
