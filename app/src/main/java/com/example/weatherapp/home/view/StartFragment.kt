package com.example.weatherapp.home.view

import android.app.Dialog
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
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.databinding.FragmentAlertBinding
import com.example.weatherapp.databinding.FragmentStartBinding
import com.example.weatherapp.databinding.LocationAlertBinding
import com.example.weatherapp.map.view.MapFragment
import com.example.weatherapp.util.SharedPreference


class StartFragment : Fragment() {

    lateinit var binding:FragmentStartBinding
    lateinit var bindingDialog:LocationAlertBinding
    lateinit var navController: NavController
    lateinit var bindingMain: ActivityMainBinding
    lateinit var location:String
    lateinit var fragment: Fragment
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
          showDialogBox()


        return binding.root    }


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
            location="map"
//            fragment = MapFragment.newInstance(location)
            SharedPreference.saveLocation(requireContext(),"map")

        }
        bindingDialog.gps.setOnClickListener {
            Toast.makeText(requireContext(),"gps", Toast.LENGTH_LONG).show()
            location="gps"
//            fragment=HomeFragment.newInstance(location)
            SharedPreference.saveLocation(requireContext(),"gps")

        }
        bindingDialog.btnSave.setOnClickListener {
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