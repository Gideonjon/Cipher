package com.example.cypher.utils

data class DataBuyResponse(
    val success: Boolean,
    val message: String,
    val data: getData

)

data class getData(
    val network: String,
    val phoneNumber: String,
    val planSize: String,
    val amount: String,
    val pin: String,
    val plan: String

)