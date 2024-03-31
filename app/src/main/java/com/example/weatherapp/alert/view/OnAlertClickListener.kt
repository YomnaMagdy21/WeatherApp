package com.example.weatherapp.alert.view

import com.example.weatherapp.model.AlertMessage
import com.example.weatherapp.model.Favorite

interface OnAlertClickListener {
    fun onClickToRemove(alert: AlertMessage)
    fun stopAlarm(alert: AlertMessage)

}