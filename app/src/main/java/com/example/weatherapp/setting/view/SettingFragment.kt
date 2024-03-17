package com.example.weatherapp.setting.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.recreate
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentAlertBinding
import com.example.weatherapp.databinding.FragmentSettingBinding
import kotlinx.coroutines.flow.MutableSharedFlow
import java.util.Locale

class SettingFragment : Fragment() {

    lateinit var binding : FragmentSettingBinding
    private val languageFlow = MutableSharedFlow<String>() // Define MutableSharedFlow

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

        binding.arabic.setOnClickListener{
            applyLanguageChanges("ar")
        }
        binding.english.setOnClickListener{
            applyLanguageChanges("en")
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
    private fun emitLanguageChange(language: String) {
        languageFlow.tryEmit(language)
    }

    // Apply language changes when the user selects a language
    private fun applyLanguageChanges(language: String) {
        saveLanguagePreference(requireContext(), language)
        emitLanguageChange(language) // Emit language change through MutableSharedFlow
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
