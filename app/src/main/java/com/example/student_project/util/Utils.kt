package com.example.student_project.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object Utils {

        @RequiresApi(Build.VERSION_CODES.O)
        fun fromDateToTime(date: String,pattern: String): String  {
            return Instant.parse(date).atZone(
                ZoneId.systemDefault()
            ).format(DateTimeFormatter.ofPattern(pattern)).toString()
        }

    fun String.containsArabic(): Boolean {
        val arabicRange =  '\u0600'..'\u06FF'
        return any { it in arabicRange }
    }


}