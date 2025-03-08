package com.example.cypher.models

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BillsClient {

    private const val BASE_URL = "https://api.swap2naira.com/api/v1/"
    private const val AUTH_TOKEN =
        "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL2FwaS5zd2FwMm5haXJhLmNvbS9hcGkvdjEvYXV0aC9sb2dpbiIsImlhdCI6MTc0MTQ0MjM1NSwiZXhwIjoxNzQ1MDQyMzU1LCJuYmYiOjE3NDE0NDIzNTUsImp0aSI6IkVLcEtGcWNrRElwVHkwbXIiLCJzdWIiOiI5NSIsInBydiI6IjIzYmQ1Yzg5NDlmNjAwYWRiMzllNzAxYzQwMDg3MmRiN2E1OTc2ZjcifQ.HNOt6D0RVWbZS9hyiNxuUhoNXAdsKEyo2oua5o8rCpw"


    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }


    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()

                // Add the hardcoded Authorization header
                requestBuilder.addHeader("Authorization", "Bearer $AUTH_TOKEN")

                chain.proceed(requestBuilder.build())
            }
            .build()
    }

    fun instance(): Api {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Parse JSON with Gson
            .client(getOkHttpClient()) // Use OkHttpClient with hardcoded token
            .build()
            .create(Api::class.java)
    }
}