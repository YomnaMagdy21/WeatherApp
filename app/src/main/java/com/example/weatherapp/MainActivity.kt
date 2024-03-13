package com.example.weatherapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.weatherapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var navController:NavController
   // lateinit var bottomNavView:BottomNavigationView
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        navController = findNavController(this, R.id.nav_home_fragment)
        setupWithNavController(binding.bottomAppBar, navController)
        binding.bottomAppBar.setOnNavigationItemSelectedListener{  item ->
            when (item.itemId) {
                R.id.home -> {
                    navController.navigate(R.id.homeFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.setting -> {

                    navController.navigate(R.id.settingFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.fav -> {

                        navController.navigate(R.id.favoriteFragment)

                    return@setOnNavigationItemSelectedListener true
                }

            }
            false

        }


    }
}