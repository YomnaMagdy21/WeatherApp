package com.example.weatherapp.alert.view

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
import com.example.weatherapp.R
import com.example.weatherapp.databinding.AlertDialogBinding
import com.example.weatherapp.databinding.FragmentAlertBinding
import com.example.weatherapp.databinding.FragmentHomeBinding


class AlertFragment : Fragment() {

    lateinit var binding : FragmentAlertBinding
    lateinit var bindingDialog : AlertDialogBinding

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
        binding = FragmentAlertBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.floatingActionButton2.setOnClickListener {
            showDialogBox()

        }
    }



    fun showDialogBox(){
        val dialog=Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
       // dialog.setContentView(R.layout.alert_dialog)
        bindingDialog = AlertDialogBinding.inflate(layoutInflater)
        dialog.setContentView(bindingDialog.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        bindingDialog.btnFrom.setOnClickListener {
            Toast.makeText(requireContext(),"from",Toast.LENGTH_LONG).show()
        }
        bindingDialog.btnTo.setOnClickListener {
            Toast.makeText(requireContext(),"to",Toast.LENGTH_LONG).show()

        }
        bindingDialog.btnSave.setOnClickListener {
            Toast.makeText(requireContext(),"save",Toast.LENGTH_LONG).show()
            dialog.dismiss()

        }
        dialog.show()

    }

    companion object {
           @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AlertFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}