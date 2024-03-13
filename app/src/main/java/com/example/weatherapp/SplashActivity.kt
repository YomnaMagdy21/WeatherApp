package com.example.weatherapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.Timer
import java.util.TimerTask

class SplashActivity : AppCompatActivity() {
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