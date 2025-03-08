package com.example.cypher.models

import com.example.cypher.utils.AirtimeBuyResponse
import com.example.cypher.utils.DataBuyResponse
import com.example.cypher.utils.DataPlanResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

data class CreateWallet(val pin: String, val username: String) // Define JSON request model

interface Api {

    @POST("create-wallet")
    fun walletResponse(@Body response: CreateWallet): Call<CreateWalletResponse>

    @GET("services/airtime/networks")
    fun getNetworkList(@Header("Authorization") token: String): Call<NetworkResponse>


    @FormUrlEncoded
    @POST("services/airtime/buy")
    fun buyAirtime(
        @Header("Authorization") authHeader: String,
        @Field("network") network: String,
        @Field("phone") phone: String,
        @Field("amount") amount: String,


        ): Call<AirtimeBuyResponse>

    @GET("services/data/plans")
    fun getDataPlan(@Header("Authorization") token: String): Call<DataPlanResponse>

    @FormUrlEncoded
    @POST("services/data/buy")
    fun buyData(
        @Header("Authorization") authHeader: String,
        @Field("network") network: String,
        @Field("phone") phone: String,
        @Field("plan_size") planSize: String,
        @Field("amount") amount: String,
        @Field("plan") plan: String,
    ): Call<DataBuyResponse>

}