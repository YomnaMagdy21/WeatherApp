package com.example.weatherapp.setting.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.recreate
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.R
import com.example.weatherapp.database.WeatherLocalDataSourceImp
import com.example.weatherapp.databinding.FragmentAlertBinding
import com.example.weatherapp.databinding.FragmentSettingBinding
import com.example.weatherapp.map.viewmaodel.MapViewModel
import com.example.weatherapp.map.viewmaodel.MapViewModelFactory
import com.example.weatherapp.model.WeatherRepositoryImp
import com.example.weatherapp.network.WeatherRemoteDataSourceImp
import com.example.weatherapp.setting.viewmodel.SettingViewModel
import com.example.weatherapp.setting.viewmodel.SettingViewModelFactory
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

class SettingFragment : Fragment() {

    lateinit var binding : FragmentSettingBinding
    private val languageFlow = MutableSharedFlow<String>()
    lateinit var settingViewModel: SettingViewModel
    lateinit var settingViewModelFactory: SettingViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(inflater, container, false)

        settingViewModelFactory= SettingViewModelFactory(
            WeatherRepositoryImp.getInstance(
            WeatherRemoteDataSourceImp.getInstance(), WeatherLocalDataSourceImp(requireContext())
            ))

        settingViewModel= ViewModelProvider(this,settingViewModelFactory).get(SettingViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.arabic.setOnClickListener {
             applyLanguageChanges("ar")
           // settingViewModel.changeLanguage("ar")
        }
        binding.english.setOnClickListener {
           // settingViewModel.changeLanguage("en")
              applyLanguageChanges("en")
        }

//        lifecycleScope.launch {
//            settingViewModel.languageChangeFlow.collectLatest { language->
//                updateAppContext(language)
//
//                recreate(requireActivity())
//            }
//        }
    }

    private fun saveLanguagePreference(context: Context, language: String) {
        val sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("language", language)
            apply()
        }
    }

    // Function to emit language changes through MutableSharedFlow
    private fun emitLanguageChange(language: String) {
        languageFlow.tryEmit(language)
    }

    // Apply language changes when the user selects a language
    private fun applyLanguageChanges(language: String) {
        saveLanguagePreference(requireContext(), language)
       // emitLanguageChange(language) // Emit language change through MutableSharedFlow
        updateAppContext(language)
    }
    private fun updateAppContext(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        recreate(requireActivity()) // Recreate activity to apply language changes
    }


}
