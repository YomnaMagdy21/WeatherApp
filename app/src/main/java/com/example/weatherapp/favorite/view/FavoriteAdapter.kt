package com.example.weatherapp.favorite.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.databinding.DayItemBinding
import com.example.weatherapp.databinding.FavItemBinding
import com.example.weatherapp.home.view.DayAdapter
import com.example.weatherapp.model.Favorite

import com.example.weatherapp.model.WeatherData
import com.example.weatherapp.model.WeatherResponse

class FavoriteAdapter(var context: Context, var listener: OnFavoriteClickListener):
    ListAdapter<Favorite, FavoriteAdapter.DayViewHolder>(DayDiffUtil()) {


    lateinit var binding: FavItemBinding


    class DayViewHolder(var binding: FavItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        binding = FavItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val current = getItem(position)

        holder.binding.city.text=current.city

        holder.binding.favCardView.setOnClickListener{

        }
//        holder.binding.tempDesc.text = current.temp.toString()
//        holder.binding.tempNo.text = current.dt.toString()
//        var iconName=current.weather[0].icon
//        Glide.with(context).load(" https://openweathermap.org/img/wn/$iconName@2x.png").into(holder.binding.imgWeather)
//        holder.binding.btnFavRemove.setOnClickListener{
//            listener.onClick(current)
//        }


    }
}

class DayDiffUtil : DiffUtil.ItemCallback<Favorite>() {
    override fun areItemsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
        return oldItem.city == newItem.city
    }

    override fun areContentsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
        return newItem == oldItem
    }

}