package com.example.weatherapp.alert.view

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.PendingIntent
import android.app.PendingIntent.getBroadcast
import android.app.TimePickerDialog
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.metrics.LogSessionId
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.alert.viewmodel.AlertViewModel
import com.example.weatherapp.alert.viewmodel.AlertViewModelFactory
import com.example.weatherapp.database.WeatherLocalDataSourceImp
import com.example.weatherapp.databinding.AlarmBinding
import com.example.weatherapp.databinding.AlertDialogBinding
import com.example.weatherapp.databinding.AlertItemBinding
import com.example.weatherapp.databinding.FragmentAlertBinding
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.example.weatherapp.favorite.view.FavoriteAdapter
import com.example.weatherapp.home.viewmodel.HomeViewModel
import com.example.weatherapp.home.viewmodel.HomeViewModelFactory
import com.example.weatherapp.model.AlertMessage
import com.example.weatherapp.model.WeatherRepositoryImp
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.network.WeatherRemoteDataSourceImp
import com.example.weatherapp.util.UIState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class AlertFragment : Fragment() ,TimePickerDialog.OnTimeSetListener,DatePickerDialog.OnDateSetListener,OnAlertClickListener{

    lateinit var binding : FragmentAlertBinding
    lateinit var bindingDialog : AlertDialogBinding
    lateinit var alertItemBinding: AlertItemBinding
    var hour=0
    var minute=0
    var day=0
    var month=0
    var year=0

    var hourSaved=0
    var minuteSaved=0
    var daySaved=0
    var monthSaved=0
    var yearSaved=0
    private var isFromButtonClicked = false
    var isAM = true
    lateinit var alertViewModel: AlertViewModel
    lateinit var alertViewModelFactory: AlertViewModelFactory
    lateinit var alert:AlertMessage
     var dateFrom:String=""
     var timeFrom:String=""
     var dateTo:String=""
     var timeTo:String=""
    lateinit var homeViewModel: HomeViewModel
    lateinit var homeViewModelFactory: HomeViewModelFactory
     var lat=0.0
    var lon=0.0
    lateinit var city:String
    lateinit var alertAdapter: AlertAdapter
    private var calendarFrom: Calendar = Calendar.getInstance()
    private var calendarTo: Calendar = Calendar.getInstance()

    var startTimeMillis:Long=0
    var isSave=false
    var d:Long=0
    lateinit var description:String
    var timeDifference:Long=0
    lateinit var bindingAlarm:AlarmBinding
    lateinit var  alarmManager:AlarmManager
    lateinit var pendingIntent:PendingIntent


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAlertBinding.inflate(inflater, container, false)
        bindingDialog = AlertDialogBinding.inflate(layoutInflater)
        bindingAlarm=AlarmBinding.inflate(layoutInflater)




        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        alertViewModelFactory = AlertViewModelFactory(
            WeatherRepositoryImp.getInstance(
                WeatherRemoteDataSourceImp.getInstance(),
                WeatherLocalDataSourceImp(requireContext())
            )
        )

        alertViewModel = ViewModelProvider(this, alertViewModelFactory).get(AlertViewModel::class.java)

        homeViewModelFactory = HomeViewModelFactory(
            WeatherRepositoryImp.getInstance(
                WeatherRemoteDataSourceImp.getInstance(),
                WeatherLocalDataSourceImp(requireContext())
            )
        )

        homeViewModel = ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)


        binding.floatingActionButton2.setOnClickListener {
            showDialogBox()

        }
//        setWeatherAlert(10,20,20)


        setUpRecyclerView()
        lifecycleScope.launch {
            alertViewModel.alert.collectLatest { result ->
                when (result) {
                    is UIState.Success<*> -> {


                        val dataList = result.data as? List<AlertMessage>
                         alertAdapter.submitList(dataList)
                    }

                    is UIState.Failure -> {
                        Toast.makeText(
                            requireContext(),
                            "there is problem in server",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.i("TAG", "onViewCreated: failure")
                    }

                    else -> {
                        Toast.makeText(
                            requireContext(),
                            "loading...",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.i("TAG", "onViewCreated: loading")
                        // if(isSuccess) {

//                                isSuccess=false
//                            }

                    }

                }
            }
        }

        lifecycleScope.launch {
            homeViewModel.weather.collectLatest { result ->
                when (result) {


                    is UIState.Success<*> -> {


                        val dataList = result.data as? WeatherResponse

                         description= dataList?.list?.get(0)?.weather?.get(0)?.description.toString()
//                        if (dataList!=null) {
//                            var alert = dataList.city.coord?.let { AlertMessage(it.lat,
//                                dataList.city.coord!!.lon,dateFrom,timeFrom,dateTo,timeTo) }
//                        }

                            lat = dataList?.city?.coord?.lat ?:0.0
                            lon = dataList?.city?.coord?.lon ?: 0.0
                            city = dataList?.city?.name.toString()
                            Log.i("TAG", "onViewCreated: dataaa $lat")
                            Log.i("TAG", "onViewCreated: dataaa $dateFrom")


                    }
                    is UIState.Failure -> {
                        Toast.makeText(
                            requireContext(),
                            "there is problem in server",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.i("TAG", "onViewCreated: failure")
                    }

                    else -> {
                        Toast.makeText(
                            requireContext(),
                            "loading...",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.i("TAG", "onViewCreated: loading")
                        // if(isSuccess) {

//                                isSuccess=false
//                            }

                    }
                }
            }
        }
       // setAlarm()
    }




   private fun showDialogBox(){
        val dialog=Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
       // dialog.setContentView(R.layout.alert_dialog)
        bindingDialog = AlertDialogBinding.inflate(layoutInflater)
        dialog.setContentView(bindingDialog.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        bindingDialog.btnFrom.setOnClickListener {
            getDateAndTime()
            Toast.makeText(requireContext(),"from",Toast.LENGTH_LONG).show()
            isFromButtonClicked = true
        }
        bindingDialog.btnTo.setOnClickListener {
            Toast.makeText(requireContext(),"to",Toast.LENGTH_LONG).show()
            getDateAndTime()

        }
        bindingDialog.btnSave.setOnClickListener {
            Toast.makeText(requireContext(),"save",Toast.LENGTH_LONG).show()
            alertViewModel.insertData(alert)
            d=calcDateAndTime(alert.fromDate,alert.fromTime)
            // setWeatherAlert(alert)
          //  setWeatherAlert(d)
            Log.i("TAG", "showDialogBox1: d2 $d")
             timeDifference = calcDateAndTime(alert.fromDate, alert.fromTime)
          // setAlarm()
            //setNotification(requireContext(), timeDifference+10000)
            Log.i("TAG", "showDialogBox1: d1 $timeDifference")
            isSave=true


            dialog.dismiss()

        }
        dialog.show()

    }

  private  fun getDateAndTime(){
        val calender=Calendar.getInstance()
        hour=calender.get(Calendar.HOUR)
        minute=calender.get(Calendar.MINUTE)
        day=calender.get(Calendar.DAY_OF_MONTH)
        month=calender.get(Calendar.MONTH)
        year=calender.get(Calendar.YEAR)

        DatePickerDialog(requireContext(),this,year,month,daySaved).show()


    }

    fun pickDate(){
        bindingDialog.btnSave.setOnClickListener {
            getDateAndTime()


        }
    }


    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {


        val calendar = Calendar.getInstance()
        var startTimeMillis:Long=0
        var endTimeMillis:Long=0
        val currentYear = calendar.get(Calendar.YEAR)
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)
        val hour12Format = if (currentHour > 12) currentHour - 12 else currentHour




        if (daySaved == calendar.get(Calendar.DAY_OF_MONTH) &&
            monthSaved == calendar.get(Calendar.MONTH) &&
            yearSaved == calendar.get(Calendar.YEAR)) {
            if (hourOfDay < hour12Format || (hourOfDay == hour12Format && minute < currentMinute)) {
                Toast.makeText(requireContext(), "Cannot select past times", Toast.LENGTH_SHORT)
                    .show()
                Log.i("TAG", "onTimeSet: hourSaved $hourOfDay and currentHour $currentHour")
                Log.i("TAG", "onTimeSet: minute $minute and minuteCurrent $currentMinute")

            }
            else {
                minuteSaved=minute
                hourSaved=hourOfDay
                val timeFormat = if (isAM) "AM" else "PM"
                val hour12Format = if (hourOfDay > 12) hourOfDay - 12 else hourOfDay
                val formattedHour = if (hour12Format == 0) 12 else hour12Format

                if(isFromButtonClicked) {
                   dateFrom="$daySaved-$monthSaved-$yearSaved"
                    bindingDialog.fromDate.text = "$daySaved-$monthSaved-$yearSaved"
                    bindingDialog.fromTime.text = String.format("%02d:%02d %s", formattedHour, minuteSaved, timeFormat)
                    timeFrom=String.format("%02d:%02d %s", formattedHour, minuteSaved, timeFormat)
//                    calendarFrom.set(yearSaved,monthSaved,daySaved,hourSaved,minuteSaved)
                 //   startTimeMillis=  calendarFrom.timeInMillis

                    isFromButtonClicked=false

                }else{
                    calendarTo.set(yearSaved,monthSaved,daySaved,hourSaved,minuteSaved)

                    dateTo="$daySaved-$monthSaved-$yearSaved"
                    bindingDialog.toDate.text = "$daySaved-$monthSaved-$yearSaved"
                    bindingDialog.toTime.text = String.format("%02d:%02d %s", hourOfDay, minuteSaved,timeFormat)
                    timeTo=String.format("%02d:%02d %s", hourOfDay, minuteSaved,timeFormat)
                    isFromButtonClicked=true
                   // endTimeMillis = calendarTo.timeInMillis
                }
            }
        }else {
               minuteSaved=minute
            hourSaved=hourOfDay
            val timeFormat = if (isAM) "AM" else "PM"
            val hour12Format = if (hourOfDay > 12) hourOfDay - 12 else hourOfDay
            val formattedHour = if (hour12Format == 0) 12 else hour12Format

            if(isFromButtonClicked) {
                       dateFrom="$daySaved-$monthSaved-$yearSaved"
                bindingDialog.fromDate.text = "$daySaved-$monthSaved-$yearSaved"
                bindingDialog.fromTime.text = String.format("%02d:%02d %s", formattedHour, minuteSaved, timeFormat)
                timeFrom=String.format("%02d:%02d %s", formattedHour, minuteSaved, timeFormat)

                isFromButtonClicked=false
            }else{
               // calendarTo.set(yearSaved,monthSaved,daySaved,hourSaved,minuteSaved)

                dateTo="$daySaved-$monthSaved-$yearSaved"
                bindingDialog.toDate.text = "$daySaved-$monthSaved-$yearSaved"
                bindingDialog.toTime.text = String.format("%02d:%02d %s", hourOfDay, minuteSaved,timeFormat)
                timeTo=String.format("%02d:%02d %s", hourOfDay, minuteSaved,timeFormat)
                isFromButtonClicked=true
                //endTimeMillis = calendarTo.timeInMillis
            }


        }
        calendarFrom.set(yearSaved,monthSaved,daySaved,hourSaved,minuteSaved)
        startTimeMillis=  calendarFrom.timeInMillis

        // Assuming calendarFrom is the Calendar instance for the start time
       // Assuming calendarTo is the Calendar instance for the end time
       // val durationMillis = calculateDuration(startTimeMillis, endTimeMillis)
        val currentTimeMillis = Calendar.getInstance().timeInMillis
        Log.i("TAG", "onTimeSet: currentTimeMillis $currentTimeMillis")
val t=startTimeMillis-currentTimeMillis
        alert= AlertMessage(lat,lon,city,dateFrom,timeFrom,dateTo,timeTo)
//        val d=calcDateAndTime(alert.fromDate,alert.toTime)
        Log.i("TAG", "onTimeSet: startTimeMillis $startTimeMillis")

        Log.i("TAG", "onTimeSet: t $t")
        Log.i("TAG", "onTimeSet: $city")

        val calendar1 = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, 1)
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
     setAlarm(minuteSaved)
        val triggerTime = calendar1.timeInMillis
        Log.i("TAG", "onTimeSet:triggerTime $triggerTime ")
       // if(isSave) {
       // setNotification(requireContext(), startTimeMillis)
      //  setWeatherAlert(startTimeMillis)
            Log.i("TAG", "onTimeSet:save ${startTimeMillis}")

      //  }

    }

    fun setNotification(context: Context, triggerTime: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlertBroadcastReceiver::class.java).apply {
            putExtra("description", description)
            putExtra("city", city)
        }
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
       // val triggerTime = System.currentTimeMillis() + timeDifference

        // Set the alarm to trigger at the calculated trigger time
        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
       // alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
        Log.i("TAG", "onTimeSet:save $triggerTime ")
    }
    @SuppressLint("ScheduleExactAlarm")
    private fun setWeatherAlert(time : Long) {
       val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
       val intent = Intent(requireContext(), AlertBroadcastReceiver::class.java)
       val pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent,
           PendingIntent.FLAG_IMMUTABLE)

       // Calculate the time to trigger the alarm (current time + 2 minutes)
      // val triggerTimeMillis = System.currentTimeMillis() + (1 * 60 * 1000) // Current time + 2 minutes

       // Set the alarm based on calculated trigger time
       alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent)

       // Log the trigger time for debugging purposes
      // Log.i("TAG", "Trigger time: ${Date(triggerTimeMillis)}")
        // Save user preferences, such as start/end time and alarm type
      //  saveAlertSettings(startTimeMillis, endTimeMillis, alarmType)
    }
// @SuppressLint("SimpleDateFormat")
//    private fun calcDateAndTime(fromDate: String, fromTime: String):Long{
//        var date =SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
//        var dateAndTime="$fromDate $fromTime"
//        var lastFormat=date.parse(dateAndTime)
    //dd-MM-yyyy hh:mm a
//        return lastFormat?.time ?: 0
//    }
//    @SuppressLint("SimpleDateFormat")
//    private fun calcDateAndTime(fromDate: String, fromTime: String): Long {
//    val dateTimeString = "$fromDate $fromTime"
//    val dateFormat = SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault())
//    val selectedTime = dateFormat.parse(dateTimeString)?.time ?: 0
//
//    val currentTime = System.currentTimeMillis()
//
//    val millisecondsDifference = -1*(selectedTime - currentTime)
//    Log.i("TAG", "calcDateAndTime: current $currentTime")
//    Log.i("TAG", "calcDateAndTime: selected $selectedTime")
//    println("Milliseconds from current time to selected time: $millisecondsDifference")
//
//    return selectedTime
//    }
@SuppressLint("SimpleDateFormat")
private fun calcDateAndTime(fromDate: String, fromTime: String): Long {
    val dateTimeString = "$fromDate $fromTime"

    val dateFormat = SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault())
    val selectedTime = dateFormat.parse(dateTimeString)?.time ?: 0

    val currentTime = System.currentTimeMillis()

    // Calculate the time difference between selected time and current time
    return selectedTime
}

  fun setAlarm(min:Int){
      val calendar = Calendar.getInstance()
      val alarmTime = System.currentTimeMillis() + (min-calendar.get(Calendar.MINUTE))*10000 +60000// 1 minute from now
      bindingAlarm.textAlarmTime.text = "city"

      // Set up AlarmManager
      alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
      val intent = Intent(requireContext(), AlertBroadcastReceiver::class.java)
     val pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE)

      intent.action = "com.example.ALARM_TRIGGERED" // Custom action string

      // Set alarm to trigger at the specified time
      alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent)

      // Dismiss button click listener
      val view = LayoutInflater.from(requireContext()).inflate(R.layout.alarm, null, false)
      val alarmMsg = view.findViewById<TextView>(R.id.text_alarm_time)
      val btnCancel = view.findViewById<Button>(R.id.button_dismiss)

      btnCancel.setOnClickListener {
          // Cancel the alarm
          alarmManager.cancel(pendingIntent)
          // Close the fragment or handle dismiss action
          requireActivity().runOnUiThread {
              Toast.makeText(requireContext(), "Alarm canceled", Toast.LENGTH_SHORT).show()
          }
      }
  }
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//
//        // Register BroadcastReceiver to handle alarm trigger event
//        val intentFilter = IntentFilter("com.example.ALARM_TRIGGERED")
//        requireContext().registerReceiver(alarmReceiver, intentFilter)
//    }
//
//    override fun onDetach() {
//        super.onDetach()
//
//        // Unregister BroadcastReceiver when fragment is detached
//        requireContext().unregisterReceiver(alarmReceiver)
//    }



    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        getDateAndTime()

        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        if (year < currentYear || (year == currentYear && month < currentMonth) || (year == currentYear && month == currentMonth && dayOfMonth < currentDayOfMonth)) {
            // Selected date is in the past, show an error message or handle it as appropriate
            Toast.makeText(requireContext(), "Cannot select past dates", Toast.LENGTH_SHORT).show()
        } else {
            // Selected date is valid, proceed to select time
            daySaved=dayOfMonth
            monthSaved=month
            yearSaved=year

            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            TimePickerDialog(requireContext(), this, hour, minute, true).show()
        } }

    fun setUpRecyclerView(){
        alertAdapter= AlertAdapter(requireContext(),this)
        binding.alertRecView.apply {
            adapter = alertAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        }

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AlertFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onClickToRemove(alert: AlertMessage) {
        alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(activity, AlertBroadcastReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent,PendingIntent.FLAG_IMMUTABLE)

        bindingAlarm.buttonDismiss.setOnClickListener {
            alarmManager.cancel(pendingIntent)
            // Close the fragment or handle dismiss action
            requireActivity().runOnUiThread {
                Toast.makeText(requireContext(), "Alarm canceled", Toast.LENGTH_SHORT).show()
            }
        }
        alertViewModel.deleteData(alert)

    }

}
