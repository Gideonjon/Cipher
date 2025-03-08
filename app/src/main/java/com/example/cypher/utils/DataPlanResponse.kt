package com.example.cypher.utils

data class DataPlanResponse(
    val success: Boolean,
    val message: String,
    val data: List<DataPlan>

)

data class DataPlan(
    val network: String,
    val network_id: String,
    val plan: String,
    val plan_id: String,
    val plan_code: String,
    val price: Double
)