package com.example.weatherapp.alert.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.AlertItemBinding
import com.example.weatherapp.databinding.FavItemBinding
import com.example.weatherapp.favorite.view.FavoriteAdapter
import com.example.weatherapp.model.AlertMessage

import com.example.weatherapp.model.WeatherData
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AlertAdapter (var context: Context,var listener: OnAlertClickListener):
    ListAdapter<AlertMessage, AlertAdapter.DayViewHolder>(DayDiffUtil()) {


    lateinit var binding: AlertItemBinding


    class DayViewHolder(var binding: AlertItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        binding = AlertItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val current = getItem(position)
        holder.binding.dateFrom.text=current.fromTime
        holder.binding.timeFrom.text=current.fromDate
        holder.binding.dateTo.text=current.toTime
        holder.binding.timeTo.text=current.toDate
        holder.binding.delete.setOnClickListener {
            listener.onClickToRemove(current)
        }
        var end=calcDateAndTime(current.toDate,current.toTime)
        if (Calendar.getInstance().timeInMillis > end) {
            listener.stopAlarm(current)
        }
//        holder.binding.tempNo.text = current.dt.toString()
//        var iconName=current.weather[0].icon
//        Glide.with(context).load(" https://openweathermap.org/img/wn/$iconName@2x.png").into(holder.binding.imgWeather)
//        holder.binding.btnFavRemove.setOnClickListener{
//            listener.onClick(current)
//        }


    }
    @SuppressLint("SimpleDateFormat")
    private fun calcDateAndTime(fromDate: String, fromTime: String):Long{
        var date = SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault())
        var dateAndTime="$fromDate $fromTime"
        var lastFormat=date.parse(dateAndTime)
        // dd-MM-yyyy hh:mm a
        return lastFormat?.time ?: 0
    }
}

class DayDiffUtil : DiffUtil.ItemCallback<AlertMessage>() {
    override fun areItemsTheSame(oldItem: AlertMessage, newItem: AlertMessage): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: AlertMessage, newItem: AlertMessage): Boolean {
        return newItem == oldItem
    }

}