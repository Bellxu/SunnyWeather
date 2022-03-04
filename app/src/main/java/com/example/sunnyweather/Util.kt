package com.example.sunnyweather.logic.model

import android.widget.Toast
import com.example.sunnyweather.SunnyWeatherApplication

fun String.shoToast(duratin:Int=Toast.LENGTH_SHORT){
    Toast.makeText(SunnyWeatherApplication.context,this,duratin).show()
}