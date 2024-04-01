package com.example.weatherapp.alert.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.AlarmBinding
import com.example.weatherapp.databinding.AlertItemBinding
import com.example.weatherapp.databinding.FavItemBinding
import com.example.weatherapp.favorite.view.FavoriteAdapter
import com.example.weatherapp.model.AlertMessage

import com.example.weatherapp.model.WeatherData
import com.example.weatherapp.util.SharedPreference
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AlertAdapter (var context: Context,var listener: OnAlertClickListener):
    ListAdapter<AlertMessage, AlertAdapter.DayViewHolder>(DayDiffUtil()) {


    lateinit var binding: AlertItemBinding
    lateinit var bindingAlarm: AlarmBinding
    lateinit var isCancel:String



    class DayViewHolder(var binding: AlertItemBinding,var bindingAlarm: AlarmBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        binding = AlertItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        bindingAlarm= AlarmBinding.inflate(LayoutInflater.from(context))
        return DayViewHolder(binding,bindingAlarm)
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

       // var end=calcDateAndTime(current.toDate,current.toTime)
        val calendar = Calendar.getInstance()
        val currentTimeMillis = calendar.getTimeInMillis()
        val alarmTime = currentTimeMillis + (4 - calendar.get(Calendar.MINUTE)) * 20000 // Convert minutes to milliseconds
        isCancel=SharedPreference.getCancel(context)
        Log.i("TAG", "onBindViewHolder: cancel ? $isCancel")
//        if (isCancel=="cancel") {
//            listener.stopAlarm(current)
//            SharedPreference.saveCancel(context,"not")
//        }
       holder.bindingAlarm.buttonDismiss.setOnClickListener {



            listener.stopAlarm(current)
           // alertViewModel.deleteData(alert)
        }



    }
//    @SuppressLint("SimpleDateFormat")
//    private fun calcDateAndTime(fromDate: String, fromTime: String):Long{
//        var date = SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault())
//        var dateAndTime="$fromDate $fromTime"
//        var lastFormat=date.parse(dateAndTime)
//        // dd-MM-yyyy hh:mm a
//        return lastFormat?.time ?: 0
//    }
}

class DayDiffUtil : DiffUtil.ItemCallback<AlertMessage>() {
    override fun areItemsTheSame(oldItem: AlertMessage, newItem: AlertMessage): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: AlertMessage, newItem: AlertMessage): Boolean {
        return newItem == oldItem
    }

}