package com.example.student_project.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.student_project.data.db.StudentDatabase
import com.example.student_project.data.db.StudentDatabaseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideStudentDao(studentDatabase: StudentDatabase): StudentDatabaseDao =
        studentDatabase.studentDao()

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): StudentDatabase =
        Room.databaseBuilder(context, StudentDatabase::class.java, "student_db")
            .fallbackToDestructiveMigration()
            .build()
}
