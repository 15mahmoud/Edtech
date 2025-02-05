package com.example.student_project.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.student_project.data.model.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class StudentDatabase: RoomDatabase()  {
    abstract fun studentDao(): StudentDatabaseDao
}