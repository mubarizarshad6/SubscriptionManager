package com.example.kotlintest.data

import com.example.kotlintest.R
import com.example.kotlintest.models.Service
import com.example.kotlintest.models.Frequency
import com.example.kotlintest.models.Category

object SampleData {

    val services = listOf(
        Service("Netflix", R.drawable.netflix, 10),
        Service("Hulu", R.drawable.hulu, 5),
        Service("Spotify", R.drawable.spotify, 6),
        Service("PlayStation+", R.drawable.playstation, 7),
        Service("Paramount+", R.drawable.paramount, 5),
        Service("YouTube Music", R.drawable.music, 20)
    )

    val frequencies = listOf(
        Frequency("Weekly"),
        Frequency("Monthly"),
        Frequency("Annually")
    )

    val categories = listOf(
        Category("Subscription", R.drawable.subscription),
        Category("Utility", R.drawable.utility),
        Category("Loan", R.drawable.loan),
        Category("Card Payment", R.drawable.card),
        Category("Rent", R.drawable.rent),
    )
}
