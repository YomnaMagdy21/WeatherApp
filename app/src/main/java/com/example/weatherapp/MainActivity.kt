package com.example.weatherapp

import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.weatherapp.database.WeatherLocalDataSourceImp
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.model.WeatherRepositoryImp
import com.example.weatherapp.network.WeatherRemoteDataSourceImp
import com.example.weatherapp.setting.viewmodel.SettingViewModel
import com.example.weatherapp.setting.viewmodel.SettingViewModelFactory
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : AppCompatActivity() {

    lateinit var navController:NavController

    lateinit var binding: ActivityMainBinding
    lateinit var settingViewModel: SettingViewModel
    lateinit var settingViewModelFactory: SettingViewModelFactory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        settingViewModelFactory= SettingViewModelFactory(
            WeatherRepositoryImp.getInstance(
                WeatherRemoteDataSourceImp.getInstance(), WeatherLocalDataSourceImp(this)
            ))

        settingViewModel= ViewModelProvider(this,settingViewModelFactory).get(SettingViewModel::class.java)
//
//        lifecycleScope.launchWhenStarted {
//            settingViewModel.languageChangeFlow.collectLatest {
//                updateAppContext(it)
//                recreate()
//            }
//        }
//
////        MainScope().launch {
//            settingViewModel.languageChangeFlow.collect { locale ->
//                // Update the context when the language changes
//                attachBaseContext(LanguageContextWrapper.wrap(baseContext, locale))
//                recreate() // Recreate the activity to reflect the language change
//            }
//        }



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
                R.id.alert -> {

                    navController.navigate(R.id.alertFragment)

                    return@setOnNavigationItemSelectedListener true
                }

            }
            false

        }


    }
    private fun updateAppContext(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        ActivityCompat.recreate(this) // Recreate activity to apply language changes
    }

}

//class LanguageContextWrapper(base: Context) : ContextWrapper(base) {
//
//    companion object {
//        fun wrap(context: Context, newLocale: String): ContextWrapper {
//            var newContext = context
//
//            val res = newContext.resources
//            val configuration = res.configuration
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                configuration.setLocale(newLocale)
//                val localeList = LocaleList(newLocale)
//                LocaleList.setDefault(localeList)
//                configuration.locales = localeList
//                newContext = newContext.createConfigurationContext(configuration)
//            } else {
//                configuration.locale = newLocale
//                res.updateConfiguration(configuration, res.displayMetrics)
//            }
//
//            return LanguageContextWrapper(newContext)
//        }
//    }
//}

