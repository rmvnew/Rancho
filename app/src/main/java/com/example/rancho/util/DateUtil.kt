package com.example.rancho.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    fun getCurrentDate():String{
        val currentDateTime = Date()
        val brazilianFormat = SimpleDateFormat("dd/MM/yyyy")
        return brazilianFormat.format(currentDateTime)
    }

    fun getCurrentTime():String{
        val currentDateTime = Date()
        val brazilianFormat = SimpleDateFormat("HH:mm:ss")
        return brazilianFormat.format(currentDateTime)
    }

}