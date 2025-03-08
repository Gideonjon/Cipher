package com.example.cypher.models

import com.example.cypher.utils.AirtimeBuyResponse
import com.example.cypher.utils.CreateWalletResponse
import com.example.cypher.utils.DataBuyResponse
import com.example.cypher.utils.DataPlanResponse
import com.example.cypher.utils.DecryptWalletResponse
import com.example.cypher.utils.NetworkResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

data class CreateWallet(val pin: String, val username: String) // Define JSON request model
data class UserLogin(val pin: String) // Define JSON request model


interface Api {

   @POST("create-wallet")
    fun walletResponse(@Body response: CreateWallet): Call<CreateWalletResponse>

    @POST("decrypt_wallet")
    fun userLogin(@Body request: UserLogin): Call<DecryptWalletResponse>


    @GET("services/airtime/networks")
    fun getNetworkList(): Call<NetworkResponse>


    @FormUrlEncoded
    @POST("services/airtime/buy")
    fun buyAirtime(
        @Field("network") network: String,
        @Field("phone") phone: String,
        @Field("amount") amount: String,


        ): Call<AirtimeBuyResponse>

    @GET("services/data/plans")
    fun getDataPlan(): Call<DataPlanResponse>

    @FormUrlEncoded
    @POST("services/data/buy")
    fun buyData(
        @Field("network") network: String,
        @Field("phone") phone: String,
        @Field("plan_size") planSize: String,
        @Field("amount") amount: String,
        @Field("plan") plan: String,
    ): Call<DataBuyResponse>

}