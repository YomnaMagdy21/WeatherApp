package com.example.weatherapp.alert.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.AlertItemBinding
import com.example.weatherapp.databinding.FavItemBinding
import com.example.weatherapp.favorite.view.FavoriteAdapter
import com.example.weatherapp.model.Day

class AlertAdapter (var context: Context):
    ListAdapter<Day, AlertAdapter.DayViewHolder>(DayDiffUtil()) {


    lateinit var binding: AlertItemBinding


    class DayViewHolder(var binding: AlertItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        binding = AlertItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val current = getItem(position)
//        holder.binding.tempDesc.text = current.temp.toString()
//        holder.binding.tempNo.text = current.dt.toString()
//        var iconName=current.weather[0].icon
//        Glide.with(context).load(" https://openweathermap.org/img/wn/$iconName@2x.png").into(holder.binding.imgWeather)
//        holder.binding.btnFavRemove.setOnClickListener{
//            listener.onClick(current)
//        }


    }
}

class DayDiffUtil : DiffUtil.ItemCallback<Day>() {
    override fun areItemsTheSame(oldItem: Day, newItem: Day): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Day, newItem: Day): Boolean {
        return newItem == oldItem
    }

}