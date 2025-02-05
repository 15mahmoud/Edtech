package com.example.student_project.util

import androidx.room.TypeConverter

class ListConverter {
    @TypeConverter
    fun fromString(value: String): List<String> {
        return value.split(",").map { it.trim() }
    }
    @TypeConverter
    fun toString(value: List<String>): String {
        return value.joinToString(",")
    }

}