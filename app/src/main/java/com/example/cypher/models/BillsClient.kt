package com.example.cypher.models

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BillsClient {

    private const val BASE_URL = "https://api.swap2naira.com/api/v1/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }


    private fun getOkHttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()

                // Retrieve the Authorization token from SharedPreferences
                val sharedPreferences =
                    context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
                val authToken = sharedPreferences.getString("auth_token", "") ?: ""

                // Add the Authorization header if the token exists
                if (authToken.isNotEmpty()) {
                    requestBuilder.addHeader("Authorization", "Bearer $authToken")
                }

                chain.proceed(requestBuilder.build())
            }
            .build()
    }


    fun instance(context: Context): Api {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Parse JSON with Gson
            .client(getOkHttpClient(context)) // Use dynamic OkHttpClient
            .build()
            .create(Api::class.java)
    }
}