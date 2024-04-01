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
import com.example.weatherapp.util.SharedPreference
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
    lateinit var alertWay:String
     var intent:Intent=Intent()


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
//        AlertBroadcastReceiver().btnCancel.setOnClickListener {
//            alertViewModel.deleteData(alert)
//        }
       // cancelAlarm()
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
       bindingDialog.alarm.setOnClickListener {
           SharedPreference.saveAlert(requireContext(),"alarm")
       }
       bindingDialog.notification.setOnClickListener {
           SharedPreference.saveAlert(requireContext(),"notification")
       }
        bindingDialog.btnSave.setOnClickListener {
            Toast.makeText(requireContext(),"save",Toast.LENGTH_LONG).show()
            alertViewModel.insertData(alert)
//            d=calcDateAndTime(alert.fromDate,alert.fromTime)
//            // setWeatherAlert(alert)
//          //  setWeatherAlert(d)
//            Log.i("TAG", "showDialogBox1: d2 $d")
//           //  timeDifference = calcDateAndTime(alert.fromDate, alert.fromTime)
//            val t=timeDifference+1000000000
//          // setAlarm(t)
            var to=triggerTime(alert.fromDate,alert.fromTime)
           // cancelAlarm(to+10000)
            var a=to.timeInMillis+2500009458
           // setAlarm(a)


            //setNotification(requireContext(), timeDifference+10000)
            Log.i("TAG", "showDialogBox1: d1 ${to.timeInMillis}")
            isSave=true
            intent.action = "com.example.ALARM_TRIGGERED"

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
               setData(hourOfDay)
            }
        }else {
               minuteSaved=minute
            hourSaved=hourOfDay

           setData(hourOfDay)

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

        calendar.set(Calendar.YEAR, yearSaved)
        calendar.set(Calendar.MONTH, monthSaved)
        calendar.set(Calendar.DAY_OF_MONTH, daySaved)
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)

        val startMillis = calendar.timeInMillis
      //  setAlarm(minuteSaved)
        Log.i("TAG", "onTimeSet: stsrt $startMillis")

    }

    fun setData(hourOfDay: Int){
        val timeFormat = if (isAM) "AM" else "PM"
        val hour12Format = if (hourOfDay > 12) hourOfDay - 12 else hourOfDay
        val formattedHour = if (hour12Format == 0) 12 else hour12Format

        if(isFromButtonClicked) {
            dateFrom="$daySaved-$monthSaved-$yearSaved"
            bindingDialog.fromDate.text = "$daySaved-$monthSaved-$yearSaved"
            bindingDialog.fromTime.text = String.format("%02d:%02d %s", formattedHour, minuteSaved, timeFormat)
            timeFrom=String.format(Locale.ENGLISH,"%02d:%02d %s", formattedHour, minuteSaved,timeFormat)

            isFromButtonClicked=false
        }else{
            // calendarTo.set(yearSaved,monthSaved,daySaved,hourSaved,minuteSaved)

            dateTo="$daySaved-$monthSaved-$yearSaved"
            bindingDialog.toDate.text = "$daySaved-$monthSaved-$yearSaved"
            bindingDialog.toTime.text = String.format("%02d:%02d %s", hourOfDay, minuteSaved,timeFormat)
            timeTo=String.format(Locale.ENGLISH,"%02d:%02d %s", hourOfDay, minuteSaved,timeFormat)
            isFromButtonClicked=true
            //endTimeMillis = calendarTo.timeInMillis
        }
    }


 @SuppressLint("SimpleDateFormat")
    private fun calcDateAndTime(fromDate: String, fromTime: String):Long{
        var date =SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault())
        var dateAndTime="$fromDate $fromTime"
        var lastFormat=date.parse(dateAndTime)
   // dd-MM-yyyy hh:mm a
        return lastFormat?.time ?: 0
    }


  @SuppressLint("ScheduleExactAlarm")
  fun setAlarm(min:Int){
      val calendar = Calendar.getInstance()
      val currentTimeMillis = calendar.getTimeInMillis()
      val alarmTime = currentTimeMillis + (min - calendar.get(Calendar.MINUTE)) * 20000 // Convert minutes to milliseconds
      bindingAlarm.textAlarmTime.text = "city"
      Log.i("TAG", "setAlarm: alarmTime: ${min - calendar.get(Calendar.MINUTE)}")
      Log.i("TAG", "setAlarm: alarmTime2: $alarmTime ")
      // Set up AlarmManager for the main alarm
       alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
      val intent = Intent(requireContext(), AlertBroadcastReceiver::class.java)
       pendingIntent = PendingIntent.getBroadcast(requireContext(), alert.id, intent, PendingIntent.FLAG_IMMUTABLE)
       // Custom action string
      alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent)

      // Calculate the time difference between current time and alarm time
      val timeDifference = alarmTime - currentTimeMillis
           //  cancelAlarm(timeDifference)
      // Set up AlarmManager for cancellation alarm


  }

//    fun cancelAlarm(){
//        val cancelIntent = Intent(requireContext(), AlertBroadcastReceiver::class.java)
//        pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, cancelIntent, PendingIntent.FLAG_IMMUTABLE)
//        val cancelTime = System.currentTimeMillis() + timeDifference // Cancellation time
//        alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
//      //  alarmManager.setExact(AlarmManager.RTC_WAKEUP, cancelTime, pendingIntent)
//        alarmManager.cancel(pendingIntent)
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



    override fun onClickToRemove(alert: AlertMessage) {
        alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(activity, AlertBroadcastReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent,PendingIntent.FLAG_IMMUTABLE)
        alarmManager.cancel(pendingIntent)
        alertViewModel.deleteData(alert)

    }

  override fun stopAlarm(alert: AlertMessage) {
      alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
      val intent = Intent(activity, AlertBroadcastReceiver::class.java)
      pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent,PendingIntent.FLAG_IMMUTABLE)

      alarmManager.cancel(pendingIntent)
        Toast.makeText(requireContext(), "Alarm canceled1", Toast.LENGTH_LONG).show()
      bindingAlarm.buttonDismiss.setOnClickListener {

          // Close the fragment or handle dismiss action
          requireActivity().runOnUiThread {
              Toast.makeText(requireContext(), "Alarm canceled", Toast.LENGTH_SHORT).show()
          }
          alertViewModel.deleteData(alert)
      }


        alertViewModel.deleteData(alert)
   }
    fun triggerTime(toDate: String, toTime: String): Calendar {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())

        val combinedDateTime = "$toDate $toTime"

        val calendar = Calendar.getInstance()

        try {
            calendar.time = dateFormat.parse(combinedDateTime) // Parse the combined date and time string
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return calendar
    }

}

