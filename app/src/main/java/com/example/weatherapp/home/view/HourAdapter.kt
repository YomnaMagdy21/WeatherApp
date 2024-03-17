package com.example.weatherapp.home.view

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.databinding.HourItemBinding
import com.example.weatherapp.model.Hour
import com.example.weatherapp.model.WeatherData
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HourAdapter (var context: Context):
    ListAdapter<WeatherData, HourAdapter.ProductViewHolder>(HourDiffUtil()) {


    lateinit var binding: HourItemBinding


    class ProductViewHolder(var binding: HourItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        binding = HourItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val current = getItem(position)

        holder.binding.degree.text = current.main.temp.toString()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val dateTime = LocalDateTime.parse(current.dt_txt, formatter)
        val time = dateTime.toLocalTime()
        holder.binding.hour.text = time.toString()
        var iconName=current.weather[0].icon

        Glide.with(context).load("https://openweathermap.org/img/wn/$iconName@2x.png").into(holder.binding.img)

//        holder.binding.btnFavRemove.setOnClickListener{
//            listener.onClick(current)
//        }


    }
}

    class HourDiffUtil : DiffUtil.ItemCallback<WeatherData>(){
        override fun areItemsTheSame(oldItem: WeatherData, newItem: WeatherData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: WeatherData, newItem: WeatherData): Boolean {
            return newItem == oldItem
        }

    }





