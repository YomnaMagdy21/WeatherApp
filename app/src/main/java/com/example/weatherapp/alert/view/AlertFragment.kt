package com.example.weatherapp.alert.view

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import com.example.weatherapp.R
import com.example.weatherapp.databinding.AlertDialogBinding
import com.example.weatherapp.databinding.FragmentAlertBinding
import com.example.weatherapp.databinding.FragmentHomeBinding
import java.util.Calendar


class AlertFragment : Fragment() ,TimePickerDialog.OnTimeSetListener,DatePickerDialog.OnDateSetListener{

    lateinit var binding : FragmentAlertBinding
    lateinit var bindingDialog : AlertDialogBinding
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




        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.floatingActionButton2.setOnClickListener {
            showDialogBox()

        }
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
            dialog.dismiss()

        }
        dialog.show()

    }

    fun getDateAndTime(){
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
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        if (hourOfDay < currentHour || (hourOfDay == currentHour && minute < currentMinute)) {
            Toast.makeText(requireContext(), "Cannot select past times", Toast.LENGTH_SHORT).show()
        } else {
               minuteSaved=minute
            hourSaved=hourOfDay
            val timeFormat = if (isAM) "AM" else "PM"
            val hour12Format = if (hourOfDay > 12) hourOfDay - 12 else hourOfDay
            val formattedHour = if (hour12Format == 0) 12 else hour12Format

            if(isFromButtonClicked) {
                bindingDialog.fromDate.text = "$daySaved-$monthSaved-$yearSaved"
                bindingDialog.fromTime.text = String.format("%02d:%02d",  formattedHour, minuteSaved, timeFormat)
                isFromButtonClicked=false
            }else{
                bindingDialog.toDate.text = "$daySaved-$monthSaved-$yearSaved"
                bindingDialog.toTime.text = String.format("%02d:%02d", hourOfDay, minuteSaved)
                isFromButtonClicked=true



            }


        }


    }



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

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AlertFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

}
