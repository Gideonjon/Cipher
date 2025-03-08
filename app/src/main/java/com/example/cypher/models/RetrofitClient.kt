package com.example.cypher.models

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://cypher-85fk.onrender.com/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }


    private fun getOkHttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .header("Accept", "application/json") // Ensure JSON responses
                    .header(
                        "Content-Type",
                        "application/x-www-form-urlencoded"
                    ) // ✅ Correct content type

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