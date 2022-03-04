package com.example.sunnyweather.ui.place

import android.app.DownloadManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.sunnyweather.logic.Repository
import com.example.sunnyweather.logic.dao.PlaceDao
import com.example.sunnyweather.logic.model.Place

class PlaceViewModel:ViewModel() {
    private val searchLiveData=MutableLiveData<String>()

    fun savePlace(place: Place){
        Repository.savePlace(place)
    }

    fun getPlace():Place{
        return Repository.getPlace()
    }

     fun isSavePlace():Boolean{
        return Repository.isSavePlace()
    }

    val placeList=ArrayList<Place>()

    val placeLiveData=Transformations.switchMap(searchLiveData){
        query ->
        Repository.searchPlaces(query)

    }

    fun searchPlaces(query: String){
        searchLiveData.value=query
    }
}