package com.example.sunnyweather.ui.weather

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.hardware.input.InputManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.example.sunnyweather.R
import com.example.sunnyweather.logic.model.Weather
import com.example.sunnyweather.logic.model.shoToast
import com.example.sunnyweather.ui.place.WeatherViewModel
import com.sunnyweather.android.logic.model.getSky
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.forecast.*
import kotlinx.android.synthetic.main.life_index.*
import kotlinx.android.synthetic.main.now.*
import java.text.SimpleDateFormat
import java.util.*

class WeatherActivity : AppCompatActivity() {

    companion object{
        fun cationStart(context:Context,location_lat:String,location_lng:String,placeName:String){
            val intent = Intent(context, WeatherActivity::class.java).apply {
            putExtra("location_lat",location_lat)
            putExtra("location_lng",location_lng)
            putExtra("place_name",placeName)
            }
            context.startActivity(intent)

        }
    }

     val viewModel by lazy { ViewModelProviders.of(this).get(WeatherViewModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val decorView = window.decorView
        decorView.systemUiVisibility=View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor=Color.TRANSPARENT

//        if (Build.VERSION.SDK_INT >= 21) {
//            val decorView = window.decorView
//            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//            window.statusBarColor = Color.TRANSPARENT
//        }
        setContentView(R.layout.activity_weather)
        if (viewModel.lat.isEmpty()) {
            viewModel.lat=intent.getStringExtra("location_lat")?:""
        }
        if (viewModel.lng.isEmpty()) {
            viewModel.lng=intent.getStringExtra("location_lng")?:""
        }
        if (viewModel.placeName.isEmpty()) {
            viewModel.placeName=intent.getStringExtra("place_name")?:""
        }

        viewModel.weatherLiveData.observe(this, Observer {
            result ->
            val weather=result.getOrNull()
            if (weather!=null) {
                showWeatherInfo(weather)
            }else{
                "????????????????????????".shoToast()
                result.exceptionOrNull()?.printStackTrace()
            }
            swipeRefresh.isRefreshing=false
        })
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary)
        refreshWeather()
        swipeRefresh.setOnRefreshListener {
            refreshWeather()
        }

        navBtn.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        drawerLayout.addDrawerListener(object :DrawerLayout.DrawerListener{
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            }

            override fun onDrawerOpened(drawerView: View) {
            }

            override fun onDrawerClosed(drawerView: View) {
                //????????????
                val manager=getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(drawerView.windowToken,InputMethodManager.HIDE_NOT_ALWAYS)
            }

            override fun onDrawerStateChanged(newState: Int) {
            }

        })
    }

    fun refreshWeather(){
        viewModel.refreshWeather(viewModel.lng,viewModel.lat)
        swipeRefresh.isRefreshing=true
    }

    private fun showWeatherInfo(weather: Weather) {
        placeName.text=viewModel.placeName
        var realtime=weather.realtimeResponse
        var daily=weather.dailyResponse
        // ??????now.xml???????????????
        val currentTempText = "${realtime.temperature.toInt()} ???"
        currentTemp.text = currentTempText
        currentSky.text = getSky(realtime.skycon).info
        val currentPM25Text = "???????????? ${realtime.airQuality.aqi.chn.toInt()}"
        currentAQI.text = currentPM25Text
        nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)
        // ??????forecast.xml??????????????????
        forecastLayout.removeAllViews()
        val days = daily.skycon.size
        for (i in 0 until days) {
            val skycon = daily.skycon[i]
            val temperature = daily.temperature[i]
            val view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false)
            val dateInfo = view.findViewById(R.id.dateInfo) as TextView
            val skyIcon = view.findViewById(R.id.skyIcon) as ImageView
            val skyInfo = view.findViewById(R.id.skyInfo) as TextView
            val temperatureInfo = view.findViewById(R.id.temperatureInfo) as TextView
//            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//            dateInfo.text = simpleDateFormat.format(skycon.date)
            val sky = getSky(skycon.value)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text = sky.info
            val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} ???"
            temperatureInfo.text = tempText
            forecastLayout.addView(view)
        }
        // ??????life_index.xml??????????????????
        val lifeIndex = daily.lifeIndex
        coldRiskText.text = lifeIndex.coldRisk[0].desc
        dressingText.text = lifeIndex.dressing[0].desc
        ultravioletText.text = lifeIndex.ultraviolet[0].desc
        carWashingText.text = lifeIndex.carWashing[0].desc
        weatherLayout.visibility = View.VISIBLE

    }
}