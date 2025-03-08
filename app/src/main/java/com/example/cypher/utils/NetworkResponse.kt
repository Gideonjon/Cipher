package com.example.cypher.utils

data class NetworkResponse(
    val success: Boolean,
    val message: String,
    val data: List<NetworkPlan>
)

data class NetworkPlan(
    val network: String,
    val id: String
)