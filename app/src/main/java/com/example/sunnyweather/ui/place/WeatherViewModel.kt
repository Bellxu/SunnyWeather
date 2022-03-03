package com.example.sunnyweather.ui.place

import android.app.DownloadManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.sunnyweather.logic.Repository
import com.example.sunnyweather.logic.model.Location
import com.example.sunnyweather.logic.model.Place

class WeatherViewModel:ViewModel() {
    private val locationLiveData=MutableLiveData<Location>()

    var placeName:String = ""

    var lng:String = ""

    var lat:String = ""


    val weatherLiveData=Transformations.switchMap(locationLiveData){
        location ->
        Repository.refreshWeather(location.lng,location.lat)
    }

    fun refreshWeather(lng: String,lat:String){
        locationLiveData.value=Location(lng, lat)
    }
}