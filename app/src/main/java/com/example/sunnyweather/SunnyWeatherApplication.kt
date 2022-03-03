package com.example.sunnyweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.example.sunnyweather.logic.network.SunnyWeatherNetwork

class SunnyWeatherApplication:Application() {

    companion object{
        const val key="FEJ4okCp4qJnaFMN"
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context=applicationContext

    }

}