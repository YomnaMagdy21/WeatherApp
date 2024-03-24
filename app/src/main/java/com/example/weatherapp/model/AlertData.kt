package com.example.weatherapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

//data class Alert(var sender_name:String,var event:String,var start:Long,var end:Long,var description:String) {
//}


//@Entity(tableName = "Alert")
data class AlertData(
//    @PrimaryKey(autoGenerate = true)
//    var id: Int,
    var date: String,
    var message: Message
)




data class Alert(
    var id: String,
    var geometry: Geometry
)


data class Description(
    var language: String,
    var event: String,
    var headline: String,
    var description: String,
    var instruction: String
)


data class Message(
    var alert: Alert,
    var msg_type: String,
    var categories: List<String>,
    var urgency: String,
    var severity: String,
    var certainty: String,
    var start: Long,
    var end: Long,
    var sender: String,
    var description: List<Description>
)


data class Geometry(
    var type: String,
    var coordinates: List<List<List<Double>>>
)

