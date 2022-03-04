package com.example.sunnyweather.ui.place

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.sunnyweather.R
import com.example.sunnyweather.logic.Repository
import com.example.sunnyweather.logic.model.Place
import com.example.sunnyweather.ui.weather.WeatherActivity

class PlaceAdapter(private val fragment: PlaceFragment, private val data:List<Place>):RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>() {

    inner class PlaceViewHolder(view: View):RecyclerView.ViewHolder(view){
        val placeName: TextView =view.findViewById(R.id.placeName)
        val placeAddress: TextView =view.findViewById(R.id.placeAddress)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.place_item, parent, false)
        val placeViewHolder = PlaceViewHolder(view)
        placeViewHolder.itemView.setOnClickListener {
            val position = placeViewHolder.adapterPosition
            val place=data[position]
            WeatherActivity.cationStart(parent.context,place.location.lat,place.location.lng,place.name)
            fragment.viewModel.savePlace(place)
            fragment.activity?.finish()
//            fragment.activity?.finish()
//            val intent = Intent(parent.context, WeatherActivity::class.java).apply {
//                putExtra("location_lat",place.location.lat)
//                putExtra("location_lng",place.location.lng)
//                putExtra("placeName",place.name)
//            }
//            fragment.startActivity(intent)
        }
        return placeViewHolder
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = data[position]
        holder.placeName.text=place.name
        holder.placeAddress.text=place.address
    }

    override fun getItemCount(): Int {
        return data.size
    }
}