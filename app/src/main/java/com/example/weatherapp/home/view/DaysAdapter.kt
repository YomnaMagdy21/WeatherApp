package com.example.weatherapp.home.view


import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.databinding.DayItemBinding

import com.example.weatherapp.model.WeatherData
import com.example.weatherapp.util.SharedPreference
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class DayAdapter (var context: Context):
    ListAdapter<WeatherData, DayAdapter.DayViewHolder>(DayDiffUtil()) {


    lateinit var binding: DayItemBinding


    class DayViewHolder(var binding: DayItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        binding = DayItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DayViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val current = getItem(position)
        val unit=SharedPreference.getUnit(context)
        holder.binding.tempDesc.text = current.weather[0].description
        if(unit=="metric") {
            holder.binding.tempNo.text =
                current.main.temp_max.toString() + "°C/" + current.main.temp_min + "°C"
        }else if(unit=="imperial"){
            holder.binding.tempNo.text =
                current.main.temp_max.toString() + "°F/" + current.main.temp_min + "°F"

        }else{
            holder.binding.tempNo.text =
                current.main.temp_max.toString() + "°K/" + current.main.temp_min + "°K"

        }
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")


        val daysToShow = mutableSetOf<String>()
        val daysToShowCount = 5 // Number of unique days to show

   val language=Locale.getDefault().language
        val appLocale = Resources.getSystem().configuration.locales[0]
        Log.i("TAG", "onBindViewHolder:appLocale $appLocale ")
        val dateTime = LocalDateTime.parse(current.dt_txt, formatter)
        val date = dateTime.toLocalDate()

        val dayOfWeekEnum = date.dayOfWeek

      if(language=="ar") {
          val dayOfWeekName = dayOfWeekEnum.getDisplayName(TextStyle.SHORT, Locale("ar"))
          holder.binding.day.text=dayOfWeekName
      }else{
          val dayOfWeekName = dayOfWeekEnum.getDisplayName(TextStyle.SHORT, Locale("en"))
          holder.binding.day.text=dayOfWeekName
      }

//        for (dateTime in current.dt_txt) {
//            val dayOfWeekName = dayOfWeekEnum.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
//            daysToShow.add(dayOfWeekName)
//            if (daysToShow.size >= daysToShowCount) {
//                break // Break loop once desired number of unique days is reached
//            }
//        current.dt_txt.map {
//            if(daysToShow.contains(dayOfWeekName)){
//                holder.binding.day.text=dayOfWeekName
//            }
//
//        }





        var iconName=current.weather[0].icon
        Glide.with(context).load("https://openweathermap.org/img/wn/$iconName@2x.png").into(holder.binding.imgWeather)
//        holder.binding.btnFavRemove.setOnClickListener{
//            listener.onClick(current)
//        }


    }
}

    class DayDiffUtil : DiffUtil.ItemCallback<WeatherData>() {
        override fun areItemsTheSame(oldItem: WeatherData, newItem: WeatherData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: WeatherData, newItem: WeatherData): Boolean {
            return newItem == oldItem
        }

    }
