package com.solovova.smart_office_main.soviews

import android.content.Context
import android.text.Editable
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.solovova.smart_office_main.dataclass.SensorIndicatorTypeEnum
import com.solovova.smart_office_main.MainActivity
import com.solovova.smart_office_main.R
import com.solovova.smart_office_main.service.Sensor

//All good
class SensorButton(context: Context) : ConstraintLayout(context) {
    private var mTextMain: TextView
    private var mButtonMain: Button
    private var mButtonDel: Button
    private var mImgBig: MutableList<ImageView>
    private var mImgSmall: MutableList<ImageView>

    private var sensor: Sensor? = null

    init {
        inflate(context, R.layout.soview_sensor_button, this)
        this.mTextMain = findViewById(R.id.textMain)
        this.mButtonMain = findViewById(R.id.button)
        this.mButtonDel = findViewById(R.id.buttonDel)

        mImgBig= mutableListOf()
        var tmpImage:ImageView =  findViewById(R.id.imageView0)
        mImgBig.add(tmpImage)
        tmpImage = findViewById(R.id.imageView1)
        mImgBig.add(tmpImage)
        tmpImage = findViewById(R.id.imageView2)
        mImgBig.add(tmpImage)
        tmpImage = findViewById(R.id.imageView3)
        mImgBig.add(tmpImage)
        mImgSmall= mutableListOf()
        tmpImage = findViewById(R.id.imageView01)
        mImgSmall.add(tmpImage)
        tmpImage = findViewById(R.id.imageView11)
        mImgSmall.add(tmpImage)
        tmpImage = findViewById(R.id.imageView21)
        mImgSmall.add(tmpImage)
        tmpImage = findViewById(R.id.imageView31)
        mImgSmall.add(tmpImage)

        val onClickListenerDel = OnClickListener {
            val sensor = this.sensor
            if (sensor != null) {
                sensor.sensorContainer.deleteSensor(sensor)
                (context as MainActivity).showStartScreen()
            }
            return@OnClickListener
        }
        this.mButtonDel.setOnClickListener(onClickListenerDel)

        val onClickListenerMain = OnClickListener {
            (context as MainActivity).fragmentsShow("FragmentSensor", sensor = sensor)
            return@OnClickListener
        }
        this.mButtonMain.setOnClickListener(onClickListenerMain)
    }

    fun refreshValue() {
        val sensor = this.sensor
        if (sensor != null) {
            var tImgBig: ImageView
            var tImgSmall: ImageView
            var tAlarm: Int
            for (t_type in SensorIndicatorTypeEnum.values()) {
                val dataIndicatorTypeDef =  sensor.sensorContainer.getDataIndicatorTypeDef(t_type)
                tImgBig = mImgBig[t_type.ordinal]
                tImgSmall = mImgSmall[t_type.ordinal]
                tAlarm = sensor.getAlarmState(t_type)

                when (tAlarm) {
                    0 -> {
                        tImgBig.visibility = View.GONE
                        tImgSmall.visibility = View.GONE
                    }
                    1,2 -> {
                        tImgBig.visibility = View.VISIBLE
                        tImgSmall.visibility = View.GONE
                        tImgSmall.background = ContextCompat.getDrawable(context, dataIndicatorTypeDef.defOnButtonAlarmIdImage[tAlarm])
                    }
                }
            }

            //Refresh describe
            val tAlarmMain = sensor.getAlarmState()
            val tHeadAlarm = arrayOf("Everything looks good","Poor","Unhealthy")
            val tHeadColor = arrayOf(R.color.colorSOGreen,R.color.colorSOYellow,R.color.colorSORed)
            val textViewExBut = findViewById<TextView>(R.id.textViewExBut)
            textViewExBut.text = Editable.Factory.getInstance().newEditable(tHeadAlarm[tAlarmMain])
            textViewExBut.setTextColor(ContextCompat.getColor(context, tHeadColor[tAlarmMain]))
        }
    }

    private fun refreshAll() {
        val sensor = this.sensor
        if (sensor != null) {
            mTextMain.text = sensor.sensorName
            refreshValue()
        }
    }

    fun setSensor(sensor: Sensor) {
        this.sensor = sensor
        this.refreshAll()
    }
}