package com.example.cypher.utils

data class AirtimeBuyResponse(
    val success: Boolean,
    val message: String,
    val data: getAirtime
)

data class getAirtime(
    val network: String,
    val phoneNumber: String,
    val amount: String,
    val pin: String,

    )

