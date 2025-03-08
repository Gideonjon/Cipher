package com.example.cypher.utils

data class CreateWalletResponse(
    val address: String,
    val seed_phrase: String,
    val username: String
)