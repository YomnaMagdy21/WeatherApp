package com.example.weatherapp.alert.view

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PixelFormat
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.R
import com.example.weatherapp.database.WeatherLocalDataSourceImp
import com.example.weatherapp.home.viewmodel.HomeViewModel
import com.example.weatherapp.home.viewmodel.HomeViewModelFactory
import com.example.weatherapp.model.WeatherRepository
import com.example.weatherapp.model.WeatherRepositoryImp
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.network.WeatherRemoteDataSourceImp
import com.example.weatherapp.util.SharedPreference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlertBroadcastReceiver : BroadcastReceiver() {

    lateinit var homeViewModel: HomeViewModel
    lateinit var homeViewModelFactory: HomeViewModelFactory
    lateinit var weatherRepository: WeatherRepository
    var lat = 0.0
    var lon = 0.0

    @RequiresApi(Build.VERSION_CODES.O)
    override  fun onReceive(context: Context?, intent: Intent?) {
        // Check if the intent is null or context is null
        if (context == null || intent == null) {
            return
        }
        //  if (intent?.action == "com.example.ALARM_TRIGGERED") {
        // Alarm triggered, execute your logic here


        lat = SharedPreference.getLat(context)
        lon = SharedPreference.getLon(context)

        weatherRepository = WeatherRepositoryImp.getInstance(
            WeatherRemoteDataSourceImp.getInstance(),
            WeatherLocalDataSourceImp(context)
        )

        val result = weatherRepository.getWeather(lat, lon, "", "ar", "standard")
        val description = intent.getStringExtra("description")
        val city = intent.getStringExtra("city")

        CoroutineScope(Dispatchers.IO).launch {
            result.collectLatest {
                val dataList = it.list.get(0).weather.get(0).description
                Log.i("TAG", "onReceive: lat $lat")
                Log.i("TAG", "onReceive: desc $dataList")
                val city = it.city.name
                Log.i("TAG", "onReceive: city $city")


                // Retrieve information from the intent (if needed)
                val message = intent.getStringExtra("message")
                if (city != null && description != null) {
                    // Create a notification channel (for Android O and above)
                    createNotification(context, description, city)
                 //   showAlarm(context)

                }

            }

        }
    }
    fun createNotification(context:Context,description:String,city:String){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "weather_alert_channel"
            val channelName = "Weather Alerts"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance)
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }

        // Build the notification
        val builder = NotificationCompat.Builder(context, "weather_alert_channel")
            .setSmallIcon(R.drawable.cloud1)
            .setContentTitle("Weather Alert")
            .setContentText(description + " " + city ?: "Weather alert message goes here")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Show the notification
        val notificationManager = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }
        notificationManager.notify(123, builder.build())


    }

    suspend fun showAlarm(context:Context){
        val LAYOUT_FLAG = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        else
            WindowManager.LayoutParams.TYPE_PHONE
        val view = LayoutInflater.from(context).inflate(R.layout.alarm, null, false)
        val windowManager = context.getSystemService(WINDOW_SERVICE) as WindowManager
        val layoutParams =
            WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )

        withContext(Dispatchers.Main) {
            windowManager.addView(view, layoutParams)
            view.visibility = View.VISIBLE
           // messageTextView.text = message
        }
    }
}



