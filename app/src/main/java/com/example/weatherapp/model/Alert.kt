package com.example.weatherapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

//data class Alert(var sender_name:String,var event:String,var start:Long,var end:Long,var description:String) {
//}


@Entity(tableName = "Alert")
data class Data(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    val date: String,
    val message: Message
)




data class Alert(
    val id: String,
    val geometry: Geometry
)


data class Description(
    val language: String,
    val event: String,
    val headline: String,
    val description: String,
    val instruction: String
)


data class Message(
    val alert: Alert,
    val msg_type: String,
    val categories: List<String>,
    val urgency: String,
    val severity: String,
    val certainty: String,
    val start: Long,
    val end: Long,
    val sender: String,
    val description: List<Description>
)


data class Geometry(
    val type: String,
    val coordinates: List<List<List<Double>>>
)

