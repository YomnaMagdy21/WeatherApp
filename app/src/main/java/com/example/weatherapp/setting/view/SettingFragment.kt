package com.example.weatherapp.setting.view

import android.content.Context
import android.os.Bundle
import android.provider.Settings.System.putString
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.recreate
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.R
import com.example.weatherapp.database.WeatherLocalDataSourceImp
import com.example.weatherapp.databinding.FragmentAlertBinding
import com.example.weatherapp.databinding.FragmentSettingBinding
import com.example.weatherapp.home.view.HomeFragment
import com.example.weatherapp.home.viewmodel.HomeViewModel
import com.example.weatherapp.home.viewmodel.HomeViewModelFactory
import com.example.weatherapp.map.view.MapFragment
import com.example.weatherapp.map.viewmaodel.MapViewModel
import com.example.weatherapp.map.viewmaodel.MapViewModelFactory
import com.example.weatherapp.model.WeatherRepositoryImp
import com.example.weatherapp.network.WeatherRemoteDataSourceImp
import com.example.weatherapp.setting.viewmodel.SettingViewModel
import com.example.weatherapp.setting.viewmodel.SettingViewModelFactory
import com.example.weatherapp.util.SharedPreference
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

class SettingFragment : Fragment() {

    lateinit var binding : FragmentSettingBinding
    private val languageFlow = MutableSharedFlow<String>()
    lateinit var settingViewModel: SettingViewModel
    lateinit var settingViewModelFactory: SettingViewModelFactory
    lateinit var homeViewModel: HomeViewModel
    lateinit var homeViewModelFactory: HomeViewModelFactory


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        homeViewModelFactory = HomeViewModelFactory(
            WeatherRepositoryImp.getInstance(
                WeatherRemoteDataSourceImp.getInstance(),
                WeatherLocalDataSourceImp(requireContext())
            )
        )

        homeViewModel = ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)

        settingViewModelFactory = SettingViewModelFactory(
            WeatherRepositoryImp.getInstance(
                WeatherRemoteDataSourceImp.getInstance(),
                WeatherLocalDataSourceImp(requireContext())
            )
        )

        settingViewModel =
            ViewModelProvider(this, settingViewModelFactory).get(SettingViewModel::class.java)


        binding.arabic.setOnClickListener {
            settingViewModel.changeLanguage("ar")
            SharedPreference.saveLanguage(requireContext(),"ar")
           // applyLanguageChanges("ar")

        }
        binding.english.setOnClickListener {
            settingViewModel.changeLanguage("en")
            SharedPreference.saveLanguage(requireContext(),"en")
            //applyLanguageChanges("en")

        }

        binding.celsius.setOnClickListener {
            SharedPreference.saveUnit(requireContext(),"metric")

        }
        binding.fahrenheit.setOnClickListener {
            SharedPreference.saveUnit(requireContext(),"imperial")

        }
        binding.kelvin.setOnClickListener {
            SharedPreference.saveUnit(requireContext(),"standard")

        }
        binding.mps.setOnClickListener {
            SharedPreference.saveWindUnit(requireContext(),"Metric")

        }
        binding.mph.setOnClickListener {
               SharedPreference.saveWindUnit(requireContext(),"Imperial")
        }
        binding.gps.setOnClickListener {
            homeViewModel.deleteData()
            SharedPreference.saveLocation(requireContext(),"gps")
        }
        binding.map.setOnClickListener {
            homeViewModel.deleteData()
            SharedPreference.saveLocation(requireContext(),"map")
            val transaction = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            transaction.replace(R.id.main, MapFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        lifecycleScope.launch {
            settingViewModel.languageChangeFlow.collect { language ->
                Log.i("TAG", "onViewCreated: language $language")
                val locale = Locale(language)
                Locale.setDefault(locale)
                val config = resources.configuration
                config.setLocale(locale)
                resources.updateConfiguration(config, resources.displayMetrics)


                recreate(requireActivity())
            }
        }
    }


        companion object {
            fun newInstance(location:String) =
                SettingFragment().apply {
                    arguments = Bundle().apply {
                        putString("units",location)
                    }
                }
        }



    private fun saveLanguagePreference(context: Context, language: String) {
        val sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("language", language)
            apply()
        }
    }

    // Function to emit language changes through MutableSharedFlow


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

