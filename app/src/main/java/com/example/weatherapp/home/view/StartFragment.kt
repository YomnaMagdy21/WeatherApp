package com.example.weatherapp.home.view

import android.app.Dialog
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.recreate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.weatherapp.R
import com.example.weatherapp.database.WeatherLocalDataSourceImp
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.databinding.FragmentAlertBinding
import com.example.weatherapp.databinding.FragmentStartBinding
import com.example.weatherapp.databinding.LocationAlertBinding
import com.example.weatherapp.home.viewmodel.HomeViewModel
import com.example.weatherapp.home.viewmodel.HomeViewModelFactory
import com.example.weatherapp.map.view.MapFragment
import com.example.weatherapp.model.WeatherRepositoryImp
import com.example.weatherapp.network.WeatherRemoteDataSourceImp
import com.example.weatherapp.util.SharedPreference
import java.util.Locale



class StartFragment : Fragment() {

    lateinit var binding:FragmentStartBinding
    lateinit var bindingDialog:LocationAlertBinding
    lateinit var navController: NavController
    lateinit var bindingMain: ActivityMainBinding
    lateinit var location:String
    lateinit var fragment: Fragment
    lateinit var homeViewModel: HomeViewModel
    lateinit var homeViewModelFactory: HomeViewModelFactory
    lateinit var language:String
    lateinit var sharedPreferences:SharedPreferences

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

        binding = FragmentStartBinding.inflate(inflater, container, false)

        homeViewModelFactory = HomeViewModelFactory(
            WeatherRepositoryImp.getInstance(
                WeatherRemoteDataSourceImp.getInstance(),
                WeatherLocalDataSourceImp(requireContext())
            )
        )

        homeViewModel = ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)



        return binding.root    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        language=SharedPreference.getLanguage(requireContext())
//              updateAppContext(language )



        showDialogBox()
    }

    private fun showDialogBox(){
        val dialog= Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        // dialog.setContentView(R.layout.alert_dialog)
        bindingDialog = LocationAlertBinding.inflate(layoutInflater)
        dialog.setContentView(bindingDialog.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        bindingDialog.map.setOnClickListener {
            Toast.makeText(requireContext(),"map", Toast.LENGTH_LONG).show()
          //  location="map"
//            fragment = MapFragment.newInstance(location)

            SharedPreference.saveLocation(requireContext(),"map")

        }
        bindingDialog.gps.setOnClickListener {
            Toast.makeText(requireContext(),"gps", Toast.LENGTH_LONG).show()
            //location="gps"
//            fragment=HomeFragment.newInstance(location)

            SharedPreference.saveLocation(requireContext(),"gps")

        }
        bindingDialog.btnSave.setOnClickListener {
            location=SharedPreference.getLocation(requireContext())

             homeViewModel.deleteData()
            Toast.makeText(requireContext(), "save", Toast.LENGTH_LONG).show()
            if(location=="map"){
                val transaction = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                transaction.replace(R.id.main, MapFragment())
                transaction.addToBackStack(null)
                transaction.commit()
            }
            dialog.dismiss()

//            val transaction = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
//            transaction.replace(R.id.main, fragment)
//            transaction.addToBackStack(null)
//            transaction.commit()



//            bindingMain = ActivityMainBinding.inflate(layoutInflater)
//
//            navController = Navigation.findNavController(requireActivity(), R.id.nav_home_fragment)
//            NavigationUI.setupWithNavController(bindingMain.bottomAppBar, navController)
//            bindingMain.bottomAppBar.setOnNavigationItemSelectedListener { item ->
//                when (item.itemId) {
//                    R.id.home -> {
//                        navController.navigate(R.id.homeFragment)
//                        return@setOnNavigationItemSelectedListener true
//                    }
//
//                    else-> false
//
//
//                }
//
//            }
        }

        dialog.show()

    }
    private fun updateAppContext(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        recreate(requireActivity()) // Recreate activity to apply language changes
    }

//    fun passLocation(loc:String){
//        val fragmentB = HomeFragment.newInstance(loc)
//        val transaction = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.main, fragmentB)
//        transaction.addToBackStack(null)
//        transaction.commit()
//    }

    companion object {
            @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StartFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}