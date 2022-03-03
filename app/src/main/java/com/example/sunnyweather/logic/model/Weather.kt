package com.example.sunnyweather.logic.model

data class Weather(val realtimeResponse: RealtimeResponse.Result.Realtime,val dailyResponse: DailyResponse.Result.Daily)
