package com.example.student_project.di

import android.content.Context
import androidx.room.Room
import com.example.student_project.data.db.StudentDatabase
import com.example.student_project.data.db.StudentDatabaseDao
import com.example.student_project.data.network.ApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

    @Singleton
    @Provides
    fun provideApiClient(client: OkHttpClient) =
        Retrofit.Builder()
            .baseUrl("https://terrific-swamp-tilapia.glitch.me/api/v1/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiClient::class.java)

    @Singleton
    @Provides
    fun provideOkHttpClient(studentDatabaseDao: StudentDatabaseDao) =
        OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val token = runBlocking { studentDatabaseDao.getCurrentStudent()?.token }
                val originalRequest: Request = chain.request()
                val requestBuilder =
                    originalRequest.newBuilder().header("Authorization", "Bearer $token")
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
}
