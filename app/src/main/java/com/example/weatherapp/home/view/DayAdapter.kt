package com.example.weatherapp.home.view


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.databinding.DayItemBinding
import com.example.weatherapp.databinding.HourItemBinding
import com.example.weatherapp.model.Day
import com.example.weatherapp.model.Hour

class DayAdapter (var context: Context):
    ListAdapter<Day, DayAdapter.DayViewHolder>(DayDiffUtil()) {


    lateinit var binding: DayItemBinding


    class DayViewHolder(var binding: DayItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        binding = DayItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val current = getItem(position)
        holder.binding.tempDesc.text = current.temp.toString()
        holder.binding.tempNo.text = current.dt.toString()
        var iconName=current.weather[0].icon
        Glide.with(context).load(" https://openweathermap.org/img/wn/$iconName@2x.png").into(holder.binding.imgWeather)
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
