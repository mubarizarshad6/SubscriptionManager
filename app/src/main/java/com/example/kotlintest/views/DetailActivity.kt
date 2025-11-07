package com.example.kotlintest.views

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlintest.R
import com.example.kotlintest.models.Selection
import com.google.gson.Gson

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val serviceIcon = findViewById<ImageView>(R.id.detail_service_icon)
        val serviceName = findViewById<TextView>(R.id.detail_service_name)
        val startDate = findViewById<TextView>(R.id.detail_start_date)
        val frequency = findViewById<TextView>(R.id.detail_frequency)
        val categoryIcon = findViewById<ImageView>(R.id.detail_category_icon)
        val categoryName = findViewById<TextView>(R.id.detail_category_name)
        val priceText = findViewById<TextView>(R.id.detail_price)

        val sharedPrefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        val json = sharedPrefs.getString("userSelection", null)

        json?.let {
            val selection = Gson().fromJson(it, Selection::class.java)
            serviceName.text = selection.serviceName
            serviceIcon.setImageResource(selection.serviceIcon)
            startDate.text = selection.startDate
            frequency.text = selection.frequency
            categoryName.text = selection.categoryName
            categoryIcon.setImageResource(selection.categoryIcon)
            priceText.text = "$${selection.price}"
        }
    }
}
