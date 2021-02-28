package com.example.rancho.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    fun getCurrentDate(cal:Calendar?):String{
       // val currentDateTime = Date()
        val brazilianFormat = SimpleDateFormat("dd/MM/yyyy")
        val newDate = cal?.time ?: Date()
        return brazilianFormat.format(newDate)
    }

    fun getCurrentTime(cal: Calendar?):String{
       // val currentDateTime = Date()
        val brazilianFormat = SimpleDateFormat("HH:mm:ss")
        val newDate = cal?.time ?: Date()
        return brazilianFormat.format(newDate)
    }

}