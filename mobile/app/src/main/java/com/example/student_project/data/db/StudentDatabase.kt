package com.example.student_project.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.student_project.data.model.User
import com.example.student_project.util.ListConverter

@Database(entities = [User::class], version = 1, exportSchema = false)
@TypeConverters(ListConverter::class)
abstract class StudentDatabase : RoomDatabase() {
    abstract fun studentDao(): StudentDatabaseDao
}
