package com.solovova.smart_office_main.service.defs

import com.solovova.smart_office_main.dataclass.SensorIndicatorTypeEnum
import com.solovova.smart_office_main.R

class Def {
    companion object {
        @JvmStatic
        fun getDef(): MutableMap<SensorIndicatorTypeEnum, SensorIndicatorDef> {
            val sensorIndicatorDef = mutableMapOf<SensorIndicatorTypeEnum, SensorIndicatorDef>()

            var tmpDataIndicatorTypeDef = SensorIndicatorDef()
            with(tmpDataIndicatorTypeDef) {
                defValue = 20.0
                defAlarmBorder = arrayOf(19.0 , 26.0)
                defTypeOfAlarm = 0
                defTextAlarm = arrayOf("Excellent" , "Too cold" , "Too hot")
                defTextAlarmIdColor = arrayOf(R.color.colorSOGreen , R.color.colorSOBlue , R.color.colorSORed)
                defTextAlarmIdImage = arrayOf(0 , R.drawable.ic_sensor_pic_blue , R.drawable.ic_sensor_pic_red)
                defOnButtonAlarmIdImage = arrayOf(0 , R.drawable.ic_sensor_pic_blue , R.drawable.ic_sensor_pic_red)
                idBigPicture = R.drawable.ic_sensor_temperature
                idBigPictureGrey = R.drawable.ic_sensor_temperature_grey
                defFormatString = "%.2f"
                defDescribeValue = "℃"
                defDescribe = "Temperature"
                defTextDescribe = "\tDescribe for Temperature"
                defGraphMaxY = 50.0
                defGraphMinY = 10.0
            }
            sensorIndicatorDef[SensorIndicatorTypeEnum.Temperature] = tmpDataIndicatorTypeDef

            tmpDataIndicatorTypeDef = SensorIndicatorDef()
            with(tmpDataIndicatorTypeDef) {
                defValue = 800.0
                defAlarmBorder = arrayOf(1000.0 , 1400.0)
                defTypeOfAlarm = 1
                defTextAlarm = arrayOf("Excellent" , "A bit dirty" , "Very dirty")
                defTextAlarmIdColor = arrayOf(R.color.colorSOGreen , R.color.colorSOYellow , R.color.colorSORed)
                defTextAlarmIdImage = arrayOf(0 , R.drawable.ic_sensor_pic_yellow , R.drawable.ic_sensor_pic_red)
                defOnButtonAlarmIdImage = arrayOf(0 , R.drawable.ic_sensor_pic_yellow , R.drawable.ic_sensor_pic_red)
                idBigPicture = R.drawable.ic_sensor_co2
                idBigPictureGrey = R.drawable.ic_sensor_co2_grey
                defFormatString = "%.0f"
                defDescribeValue = "ppm"
                defDescribe = "Co2"
                defGraphMaxY = 2000.0
                defGraphMinY = 0.0
                defTextDescribe =
                    "\tCarbon dioxide at levels that are unusually high indoors may cause occupants to grow drowsy, to get headaches, or to function at lower activity levels. \n \tOutdoor CO2 levels are usually 350-450 ppm whereas the maximum indoor CO2 level considered acceptable is 1000 ppm. Keep this value lowe as possible."
            }
            sensorIndicatorDef[SensorIndicatorTypeEnum.Co2] = tmpDataIndicatorTypeDef

            tmpDataIndicatorTypeDef = SensorIndicatorDef()
            with(tmpDataIndicatorTypeDef) {
                defValue = 400.0
                defAlarmBorder = arrayOf(200.0 , 600.0)
                defTypeOfAlarm = 0
                defTextAlarm = arrayOf("Excellent" , "Too dark" , "Too shine")
                defTextAlarmIdColor = arrayOf(R.color.colorSOGreen , R.color.colorSOBlue , R.color.colorSORed)
                defTextAlarmIdImage = arrayOf(0 , R.drawable.ic_sensor_pic_blue , R.drawable.ic_sensor_pic_red)
                defOnButtonAlarmIdImage = arrayOf(0 , R.drawable.ic_sensor_pic_blue , R.drawable.ic_sensor_pic_red)
                idBigPicture = R.drawable.ic_sensor_brightness
                idBigPictureGrey = R.drawable.ic_sensor_brightness_grey
                defFormatString = "%.0f"
                defDescribeValue = "lx"
                defDescribe = "Brightness"
                defTextDescribe = "\tDescribe for Brightness"
                defGraphMaxY = 2000.0
                defGraphMinY = 100.0
            }
            sensorIndicatorDef[SensorIndicatorTypeEnum.Brightness] = tmpDataIndicatorTypeDef

            tmpDataIndicatorTypeDef = SensorIndicatorDef()
            with(tmpDataIndicatorTypeDef) {
                defValue = 50.0
                defAlarmBorder = arrayOf(70.0 , 90.0)
                defTypeOfAlarm = 1
                defTextAlarm = arrayOf("Excellent" , "Too wet" , "Very wet")
                defTextAlarmIdColor = arrayOf(R.color.colorSOGreen , R.color.colorSOYellow , R.color.colorSORed)
                defTextAlarmIdImage = arrayOf(0 , R.drawable.ic_sensor_pic_yellow , R.drawable.ic_sensor_pic_red)
                defOnButtonAlarmIdImage = arrayOf(0 , R.drawable.ic_sensor_pic_yellow , R.drawable.ic_sensor_pic_red)
                idBigPicture = R.drawable.ic_sensor_humidity
                idBigPictureGrey = R.drawable.ic_sensor_humidity_grey
                defFormatString = "%.0f"
                defDescribeValue = "%"
                defDescribe = "Humidity"
                defTextDescribe = "\tDescribe for Humidity"
                defGraphMaxY = 100.0
                defGraphMinY = 0.0
            }
            sensorIndicatorDef[SensorIndicatorTypeEnum.Humidity] = tmpDataIndicatorTypeDef
            return sensorIndicatorDef
        }
    }
}