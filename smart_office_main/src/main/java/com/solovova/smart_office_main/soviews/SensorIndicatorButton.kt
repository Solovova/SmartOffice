package com.solovova.smart_office_main.soviews

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.solovova.smart_office_main.service.SensorIndicator
import com.solovova.smart_office_main.MainActivity
import com.solovova.smart_office_main.R

class SensorIndicatorButton(context: Context) : ConstraintLayout(context) {
    private var visualTextViewUp: TextView
    private var visualImageBig: ImageView
    private var visualAlarmImage: ImageView
    private var visualAlarmText: TextView
    private var visualAlarmTextEx: TextView
    private var visualTextValue: TextView
    private var visualTextValueSign: TextView

    private var sensorIndicator: SensorIndicator? = null

    init {
        inflate(context, R.layout.soview_sensor_indicator_button, this)
        this.visualTextViewUp = findViewById(R.id.textView_Up)
        this.visualImageBig = findViewById(R.id.imageView)
        this.visualAlarmImage = findViewById(R.id.imageView3)
        this.visualAlarmText = findViewById(R.id.textView)
        this.visualAlarmTextEx = findViewById(R.id.textViewEx)
        this.visualTextValue = findViewById(R.id.textView_0)
        this.visualTextValueSign = findViewById(R.id.textView_1)
    }

    fun refreshValue() {
        val sensorIndicator = this.sensorIndicator
        if (sensorIndicator != null) {
            visualTextValue.text = String.format(sensorIndicator.sensorIndicatorDef.defFormatString, sensorIndicator.getIndicatorValue())
            val alarmCode = sensorIndicator.getAlarmCode()
            if (alarmCode != 0) {
                this.visualAlarmImage.visibility = View.VISIBLE
                this.visualAlarmText.visibility = View.VISIBLE
                this.visualAlarmTextEx.visibility = View.GONE
                this.visualAlarmText.setTextColor(ContextCompat.getColor(context, sensorIndicator.sensorIndicatorDef.defTextAlarmIdColor[alarmCode]))
                this.visualAlarmText.text = sensorIndicator.sensorIndicatorDef.defTextAlarm[alarmCode]
                this.visualAlarmImage.setBackgroundResource(sensorIndicator.sensorIndicatorDef.defTextAlarmIdImage[alarmCode])
            }else{
                this.visualAlarmImage.visibility = View.GONE
                this.visualAlarmText.visibility = View.GONE
                this.visualAlarmTextEx.visibility = View.VISIBLE
                this.visualAlarmTextEx.setTextColor(ContextCompat.getColor(context, sensorIndicator.sensorIndicatorDef.defTextAlarmIdColor[alarmCode]))
                this.visualAlarmTextEx.text = sensorIndicator.sensorIndicatorDef.defTextAlarm[alarmCode]
            }
        }
    }

    private fun refreshAll() {
        val sensorIndicator = this.sensorIndicator
        if (sensorIndicator != null) {
            val dataIndicatorTypeDef =  sensorIndicator.sensor.sensorContainer.getDataIndicatorTypeDef(sensorIndicator.typeEnum)
            this.visualTextViewUp.text = dataIndicatorTypeDef.defDescribe
            this.visualImageBig.setImageResource(dataIndicatorTypeDef.idBigPicture)
            this.visualTextValueSign.text = dataIndicatorTypeDef.defDescribeValue
            refreshValue()
        }
    }

    fun setSensorIndicator(_sensorIndicator: SensorIndicator) {
        if (this.sensorIndicator != _sensorIndicator) {
            this.sensorIndicator = _sensorIndicator

            val ivMain = findViewById<ConstraintLayout>(R.id.ivMain)
            val onClickListenerMain = OnClickListener {
                (context as MainActivity).fragmentsShow("FragmentSensorIndicatorInfo", sensorIndicator = _sensorIndicator)
                return@OnClickListener
            }

            ivMain.setOnClickListener(onClickListenerMain)
            this.refreshAll()
        }
    }


}