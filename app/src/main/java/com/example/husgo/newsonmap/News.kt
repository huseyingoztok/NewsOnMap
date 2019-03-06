package com.example.husgo.newsonmap


import java.io.Serializable
import java.util.*

data class News(var newTitle:String,var newContent:String,var lat:Double,var longitude:Double,var createdAt:String):Serializable{

}