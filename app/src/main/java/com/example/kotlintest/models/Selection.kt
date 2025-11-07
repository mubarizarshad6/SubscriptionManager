package com.example.kotlintest.models

data class Selection(
    val serviceName: String,
    val serviceIcon: Int,
    val startDate: String,
    val frequency: String,
    val categoryName: String,
    val categoryIcon: Int,
    val price : Int
)
