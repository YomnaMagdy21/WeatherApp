package com.example.weatherapp.alert.view

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
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

class AlertBroadcastReceiver : BroadcastReceiver() {

    lateinit var homeViewModel: HomeViewModel
    lateinit var homeViewModelFactory: HomeViewModelFactory
    lateinit var weatherRepository: WeatherRepository
     var lat=0.0
    var lon=0.0

    override fun onReceive(context: Context?, intent: Intent?) {
        // Check if the intent is null or context is null
        if (context == null || intent == null) {
            return
        }
        lat=SharedPreference.getLat(context)
        lon=SharedPreference.getLon(context)

         weatherRepository=WeatherRepositoryImp.getInstance(WeatherRemoteDataSourceImp.getInstance(),WeatherLocalDataSourceImp(context))

val result =weatherRepository.getWeather(lat,lon,"","ar","standard")


        CoroutineScope(Dispatchers.IO).launch {
            result.collectLatest {
                val dataList = it.list.get(0).weather.get(0).description
                Log.i("TAG", "onReceive: lat $lat")
                Log.i("TAG", "onReceive: desc $dataList")
                val city=it.city.name
                Log.i("TAG", "onReceive: city $city")



                // Retrieve information from the intent (if needed)
        val message = intent.getStringExtra("message")

        // Create a notification channel (for Android O and above)
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
            .setContentText(dataList ?: "Weather alert message goes here")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Show the notification
        val notificationManager = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(
               context ,
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
        }
    }
}