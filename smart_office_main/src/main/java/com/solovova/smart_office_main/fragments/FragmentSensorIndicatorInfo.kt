package com.solovova.smart_office_main.fragments

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.solovova.smart_office_main.MainActivity
import com.solovova.smart_office_main.R

class FragmentSensorIndicatorInfo : FragmentParent() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sensor_indicator_info, container, false)
    }

    override fun onShow() {
        super.onShow()
        val mView = view
        if (mView != null) {
            val sensorIndicator = this.sensorIndicator
            if (sensorIndicator != null) {
                val topTxName = mView.findViewById(R.id.topTxName) as TextView
                topTxName.text = Editable.Factory.getInstance().newEditable(sensorIndicator.typeEnum.toString())

                val topImMain = mView.findViewById(R.id.topImMain) as ImageView
                topImMain.setImageResource(sensorIndicator.sensorIndicatorDef.idBigPicture)

                val topTxText = mView.findViewById(R.id.topTxText) as TextView
                topTxText.text = Editable.Factory.getInstance().newEditable(sensorIndicator.sensorIndicatorDef.defTextDescribe)
                // Set value
                val textRect1 = mView.findViewById(R.id.textRect1) as ImageView
                //val param = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
                val param = textRect1.layoutParams as ConstraintLayout.LayoutParams
                //Log.i("FERROR",sensorIndicator.getAlarmCode().toString())
                when (sensorIndicator.getAlarmCode()) {
                    0 -> {
                        param.startToStart = R.id.bar0
                        param.endToEnd = R.id.bar0
                    }
                    1 -> {
                        param.startToStart = R.id.bar1
                        param.endToEnd = R.id.bar1
                    }
                    2 -> {
                        param.startToStart = R.id.bar2
                        param.endToEnd = R.id.bar2
                    }
                }

                textRect1.layoutParams = param

                val topTxValue = mView.findViewById(R.id.topTxValue) as TextView
                val strValue = "${String.format(sensorIndicator.sensorIndicatorDef.defFormatString, sensorIndicator.getIndicatorValue())} ${sensorIndicator.sensorIndicatorDef.defDescribeValue}"

                topTxValue.text = Editable.Factory.getInstance().newEditable(strValue)
                //END Set value


                val onClickListenerBack = View.OnClickListener {
                    (context as MainActivity).onBackPressed()
                }

                val buttonBack = mView.findViewById(R.id.buttonBack) as ImageView
                buttonBack.setOnClickListener(onClickListenerBack)

                val buttonOk = mView.findViewById(R.id.buttonOk) as Button
                buttonOk.setOnClickListener(onClickListenerBack)

                // Graph

                sensorIndicator.setLinkToViewGraph(mView.findViewById(R.id.graphContainer))
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentSensorIndicatorInfo()
    }
}
