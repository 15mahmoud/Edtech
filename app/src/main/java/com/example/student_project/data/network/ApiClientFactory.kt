package com.example.student_project.data.network

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

object ApiClientFactory {


//    class ErrorInterceptor : Interceptor {
//        override fun intercept(chain: Interceptor.Chain): Response {
//            val request: Request = chain.request()
//            val response: Response = chain.proceed(request)
//            //this for logging error
//            when (response.code) {
//                400 -> {
//                    TODO()
//                }
//
//                401 -> {
//                    TODO()
//                }
//
//                403 -> {
//                    TODO()
//                }
//
//            }
//            return response
//        }
//    }
//    class CacheInterceptor:Interceptor{
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val response:Response = chain.proceed(chain.request())
//        val cacheControl = CacheControl.Builder().maxAge(10,TimeUnit.DAYS)
//            .build()
//        return response.newBuilder().header("Cache-Control",cacheControl.toString()).build()
//    }
//}
//
//    private val myClient = OkHttpClient.Builder().cache(Cache(File(applicationContext.cacheDir,"http-cache"),10L*1024L*1024L))
//        .addNetworkInterceptor(CacheInterceptor()).build()

//    class AuthTokenInterceptor:Interceptor{
//        override fun intercept(chain: Interceptor.Chain): Response {
//            val token = ""
//            val originalRequest:Request = chain.request()
//            val requestBuilder = originalRequest.newBuilder().header("Authorization","Bearer $token")
//            val request= requestBuilder.build()
//            return chain.proceed(request)
//        }
//
//    }

//    class RefreshInterceptor:Interceptor{
//        override fun intercept(chain: Interceptor.Chain): Response {
//            val accessToken = "your_access_token"
//            val request = chain.request().newBuilder()
//                .header("Authorization", "Bearer $accessToken")
//                .build()
//            val response = chain.proceed(request)
//            if (response.code == 401){
//                val newToken = "your_new_access_token"
//                if (newToken != null){
//                    val newRequest = chain.request().newBuilder().header("Authorization",newToken).build()
//                    return chain.proceed(newRequest)
//                }
//            }
//            return response
//        }
//
//    }
//    private val logger = HttpLoggingInterceptor().apply {
//        level = HttpLoggingInterceptor.Level.BODY
//    }



}
