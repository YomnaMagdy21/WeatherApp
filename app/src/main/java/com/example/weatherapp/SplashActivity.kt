package com.example.weatherapp

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import com.example.weatherapp.databinding.AlertDialogBinding
import com.example.weatherapp.databinding.LocationAlertBinding
import com.example.weatherapp.home.view.StartFragment
import java.util.Timer
import java.util.TimerTask

class SplashActivity : AppCompatActivity() {

    lateinit var bindingDialog : LocationAlertBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val myTimer = Timer()

        myTimer.schedule(object : TimerTask() {
            override fun run() {
                val intent=Intent(this@SplashActivity,MainActivity::class.java)
                startActivity(intent)


            }
        }, 5200L)
    }


}