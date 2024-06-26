package com.example.weatherapp.alert.view

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PixelFormat
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.R
import com.example.weatherapp.alert.viewmodel.AlertViewModel
import com.example.weatherapp.alert.viewmodel.AlertViewModelFactory
import com.example.weatherapp.database.WeatherLocalDataSourceImp
import com.example.weatherapp.databinding.AlarmBinding
import com.example.weatherapp.databinding.AlertDialogBinding
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
import kotlin.math.log

class AlertBroadcastReceiver() : BroadcastReceiver() {

    lateinit var homeViewModel: HomeViewModel
    lateinit var homeViewModelFactory: HomeViewModelFactory
    lateinit var weatherRepository: WeatherRepository

    lateinit var language: String
    lateinit var  unit:String
    var lat:Double=0.0
    var lon:Double=0.0

    lateinit var   btnCancel:Button

    lateinit var alertWay:String
 //   lateinit var bindingAlarm: AlarmBinding
 lateinit var bindingDialog : AlertDialogBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override  fun onReceive(context: Context, intent: Intent?) {

        // Check if the intent is null or context is null
        if (context == null || intent == null) {
            return
        }
        //  if (intent?.action == "com.example.ALARM_TRIGGERED") {
            // Alarm triggered, execute your logic here


              alertWay= context?.let { SharedPreference.getAlert(it) }.toString()

            lat = SharedPreference.getLat(context)
            lon = SharedPreference.getLon(context)
            unit = SharedPreference.getUnit(context)

            language = SharedPreference.getLanguage(context)

            weatherRepository = WeatherRepositoryImp.getInstance(
                WeatherRemoteDataSourceImp.getInstance(),
                WeatherLocalDataSourceImp(context)
            )

            val result = weatherRepository.getWeather(lat, lon, "", unit, language)
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
                    //if (city != null && description != null) {
                    // Create a notification channel (for Android O and above)
                    Log.i("TAG", "onReceive: alertWay $alertWay ")
                    if (alertWay == "notification") {
                        createNotification(context, dataList, city)
                    } else {

                        showAlarm(context, dataList, city)
                    }

                    //  }

                }

            }
       // }
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

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun showAlarm(context:Context,d:String,c:String){

        // Check for the permission and request it if not granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(context)) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.packageName))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Add this flag
            context.startActivity(intent)
            return  // Return if the permission is not granted yet
        }

        val LAYOUT_FLAG = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        else
            WindowManager.LayoutParams.TYPE_PHONE
        val view = LayoutInflater.from(context).inflate(R.layout.alarm, null, false)
        var alarmMsg=view.findViewById(R.id.text_alarm_time)  as TextView
        alarmMsg.text=d+" "+c
         btnCancel=view.findViewById(R.id.button_dismiss)  as Button
        btnCancel.setBackgroundColor(Color.rgb(138,199,219))
        btnCancel.shadowRadius



        val windowManager = context.getSystemService(WINDOW_SERVICE) as WindowManager
        val layoutParams =
            WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )

        setAlarmUI(layoutParams)

        val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TITLE_COLUMN_INDEX)
        val ringtone = RingtoneManager.getRingtone(context.applicationContext, ringtoneUri)

        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val originalIntent = Intent(context, AlertBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, originalIntent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager?.cancel(pendingIntent)

        withContext(Dispatchers.Main) {
            windowManager.addView(view, layoutParams)
            view.visibility = View.VISIBLE
            ringtone.play()
           // messageTextView.text = message
        }
        SharedPreference.saveCancel(context,"not")
        btnCancel.setBackgroundResource(R.drawable.rounded_button)
        btnCancel.setOnClickListener {
            view.visibility=View.GONE
            ringtone.stop()
            SharedPreference.saveCancel(context,"cancel")


        }


    }


    fun setAlarmUI(layoutParams:WindowManager.LayoutParams){
        layoutParams.alpha=0.9f
        layoutParams.horizontalWeight=50f

        layoutParams.windowAnimations=20

        layoutParams.width=750
        layoutParams.buttonBrightness=20f
        layoutParams.gravity=Gravity.CENTER_HORIZONTAL
        layoutParams.gravity = Gravity.TOP
    }
}



