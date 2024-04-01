package com.example.weatherapp

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.databinding.ActivitySplashBinding
import com.example.weatherapp.databinding.AlertDialogBinding
import com.example.weatherapp.databinding.LocationAlertBinding
import com.example.weatherapp.home.view.StartFragment
import com.example.weatherapp.map.view.MapFragment
import com.example.weatherapp.setting.viewmodel.SettingViewModel

import com.example.weatherapp.util.SharedPreference
import java.util.Locale
import java.util.Timer
import java.util.TimerTask

class SplashActivity : AppCompatActivity() {

    lateinit var bindingDialog : LocationAlertBinding
    lateinit var location:String
    lateinit var language:String
    lateinit var binding:ActivitySplashBinding
    lateinit var initialTime:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        language=SharedPreference.getLanguage(this)
      // language= MyApplication().getAppViewModel().toString()

        Log.i("TAG", "onCreate:$language")
        updateAppContext(language)
        initialTime=SharedPreference.getInitialTime(this)
        Log.i("TAG", "onCreate: KEY_INITIAL $initialTime")

        val myTimer = Timer()

        myTimer.schedule(object : TimerTask() {
            override fun run() {
//                val intent=Intent(this@SplashActivity,MainActivity::class.java)
//                startActivity(intent)
                if(initialTime=="second") {
                    val intent=Intent(this@SplashActivity,MainActivity::class.java)
                    startActivity(intent)


                }else{

                    runOnUiThread {
                        showDialogBox()
                    }
                    SharedPreference.saveInitialTime(this@SplashActivity,"second")

                }
               // binding.animation.visibility= View.GONE

            }
        }, 8200L)
    }

    private fun showDialogBox(){
        binding.animation.visibility = View.GONE
        val dialog= Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        // dialog.setContentView(R.layout.alert_dialog)
        bindingDialog = LocationAlertBinding.inflate(layoutInflater)
        dialog.setContentView(bindingDialog.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        bindingDialog.map.setOnClickListener {
            Toast.makeText(this,"map", Toast.LENGTH_LONG).show()
            //  location="map"
//            fragment = MapFragment.newInstance(location)

            SharedPreference.saveLocation(this,"map")

        }
        bindingDialog.gps.setOnClickListener {
            Toast.makeText(this,"gps", Toast.LENGTH_LONG).show()
            //location="gps"
//            fragment=HomeFragment.newInstance(location)

            SharedPreference.saveLocation(this,"gps")

        }
        bindingDialog.btnSave.setOnClickListener {
            location= SharedPreference.getLocation(this)

            // homeViewModel.deleteData()
            Toast.makeText(this, "save", Toast.LENGTH_LONG).show()

            dialog.dismiss()

            if(location=="map"){
                val transaction = (this as AppCompatActivity).supportFragmentManager.beginTransaction()
                transaction.replace(R.id.splash, MapFragment())
                transaction.addToBackStack(null)
                transaction.commit()
            }
            else{
                val intent=Intent(this@SplashActivity,MainActivity::class.java)
                startActivity(intent)
            }
        }

        dialog.show()

    }
    private fun updateAppContext(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

}