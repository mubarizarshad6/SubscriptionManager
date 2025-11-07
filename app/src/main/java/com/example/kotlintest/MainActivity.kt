package com.example.kotlintest

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.FrameLayout
import android.widget.ImageView
import com.example.kotlintest.data.SampleData
import com.example.kotlintest.models.Service
import com.example.kotlintest.models.Frequency
import com.example.kotlintest.models.Category
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.kotlintest.models.Selection
import com.example.kotlintest.views.DetailActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private var selectedService: Service? = null
    private var selectedFrequency: Frequency? = null
    private var selectedCategory: Category? = null
    private var selectedStartDate: String = ""
    private var selectedPrice: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        updateSaveButtonState()
        val addButton = findViewById<ImageView>(R.id.add)
        addButton.setOnClickListener {
            showServicesBottomSheet()
        }
        val serviceLayout = findViewById<LinearLayout>(R.id.chooseServ)
        serviceLayout.setOnClickListener {
            showServicesBottomSheet()
        }
        val catLayout = findViewById<LinearLayout>(R.id.liner_cat)
        catLayout.setOnClickListener {
            showCategoryBottomSheet()
        }
        val servLayout = findViewById<LinearLayout>(R.id.linear_serv)
        servLayout.setOnClickListener {
            showServicesBottomSheet()
        }
        val nameLayout = findViewById<LinearLayout>(R.id.linear_name)
        nameLayout.setOnClickListener {
            showServicesBottomSheet()
        }
        findViewById<TextView>(R.id.tv_start_date).setOnClickListener {
            showDateBottomSheet()
        }
        val frequencyLayout = findViewById<LinearLayout>(R.id.fre)
        frequencyLayout.setOnClickListener {
            showFrequencyBottomSheet()
        }
        val detailButton = findViewById<Button>(R.id.detail)
        detailButton.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java)
            startActivity(intent)
        }
        findViewById<TextView>(R.id.frequency).setOnClickListener {
            showFrequencyBottomSheet()
        }
        findViewById<TextView>(R.id.subscription).setOnClickListener {
            showCategoryBottomSheet()
        }
        findViewById<TextView>(R.id.btn_save).setOnClickListener {
            if (selectedService != null && selectedFrequency != null && selectedCategory != null && selectedStartDate.isNotEmpty()) {

                val selection = Selection(
                    serviceName = selectedService!!.name,
                    serviceIcon = selectedService!!.iconRes,
                    startDate = selectedStartDate,
                    frequency = selectedFrequency!!.name,
                    categoryName = selectedCategory!!.name,
                    categoryIcon = selectedCategory!!.iconRes,
                    price = selectedPrice
                )
                // Save object as JSON
                val gson = com.google.gson.Gson()
                val json = gson.toJson(selection)
                val sharedPrefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                sharedPrefs.edit().putString("userSelection", json).apply()
                // Open DetailActivity
                val intent = Intent(this, DetailActivity::class.java)
                startActivity(intent)

            } else {
                Toast.makeText(this, "Please select all fields", Toast.LENGTH_SHORT).show()
            }
        }




    }




    private fun updateSaveButtonState() {
        val saveButton = findViewById<TextView>(R.id.btn_save)
        val allSelected = selectedService != null && selectedFrequency != null &&
                selectedCategory != null && selectedStartDate.isNotEmpty()
//        saveButton.isEnabled = allSelected
        saveButton.setTextColor(
            if (allSelected)
                ContextCompat.getColor(this, R.color.save_clr)
            else
                ContextCompat.getColor(this, R.color.gray)
        )
    }

    private fun showServicesBottomSheet() {
        val dialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        val view = LayoutInflater.from(this).inflate(R.layout.layout_bottom_sheet_services, null)
        dialog.setContentView(view)

        val doneButton = view.findViewById<TextView>(R.id.btn_done)
        val serviceList = view.findViewById<LinearLayout>(R.id.service_list)

        val mainIcon = findViewById<ImageView>(R.id.add)
        val mainText = findViewById<TextView>(R.id.tv_selected_service)
        val priceText = findViewById<TextView>(R.id.price)
        val cardprice = findViewById<TextView>(R.id.main_price)

        var selectedItem: View? = null
        var selectedService: Service? = this.selectedService

        SampleData.services.forEachIndexed { index, service ->
            val itemView = LayoutInflater.from(this)
                .inflate(R.layout.item_service, serviceList, false)

            val nameView = itemView.findViewById<TextView>(R.id.service_name)
            val iconView = itemView.findViewById<ImageView>(R.id.service_icon)
            val checkCircle = itemView.findViewById<View>(R.id.check_circle)

            nameView.text = service.name
            iconView.setImageResource(service.iconRes)

            if (this.selectedService != null && this.selectedService?.name == service.name) {
                checkCircle.visibility = View.VISIBLE
                selectedItem = itemView
            }

            itemView.setOnClickListener {
                selectedItem?.findViewById<View>(R.id.check_circle)?.visibility = View.GONE
                checkCircle.visibility = View.VISIBLE
                selectedItem = itemView
                selectedService = service
                priceText.text = "$${service.price}"
            }

            serviceList.addView(itemView)

            if (index < SampleData.services.size - 1) {
                val divider = View(this)
                divider.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    (1 * resources.displayMetrics.density).toInt()
                )
                divider.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray))
                serviceList.addView(divider)
            }
        }

        doneButton.setOnClickListener {
            selectedService?.let { service ->
                mainIcon.setImageResource(service.iconRes)
                mainIcon.background = null
                mainIcon.imageTintMode = null
                mainText.text = service.name
                cardprice.text = "$${service.price}"
                this.selectedService = service
                this.selectedPrice = service.price
                val serviceText = findViewById<TextView>(R.id.service)
                serviceText.text = service.name
                priceText.text = "$${service.price}"
                this.selectedService = service
            }

            if (selectedService == null) {
                Toast.makeText(this, "Please select a service", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            updateSaveButtonState()
            dialog.dismiss()
        }

        dialog.setOnShowListener {
            val bottomSheet = dialog.findViewById<FrameLayout>(
                com.google.android.material.R.id.design_bottom_sheet
            )
            bottomSheet?.let { sheet ->
                val screenHeight = resources.displayMetrics.heightPixels
                sheet.layoutParams.height = (screenHeight * 0.8).toInt()
                sheet.requestLayout()
            }
        }

        dialog.show()
    }

    private fun showDateBottomSheet() {
        val dialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        val view = LayoutInflater.from(this).inflate(R.layout.layout_bottom_sheet_date_picker, null)
        dialog.setContentView(view)

        val datePicker = view.findViewById<DatePicker>(R.id.date_picker)
        val doneButton = view.findViewById<TextView>(R.id.btn_done)
        val tvStartDate = findViewById<TextView>(R.id.tv_start_date)

        val calendar = Calendar.getInstance()

        datePicker.init(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ) { _, year, month, day ->
            calendar.set(year, month, day)
        }

        doneButton.setOnClickListener {
            val formatted =
                SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(calendar.time)
            tvStartDate.text = formatted
            this.selectedStartDate = formatted
            updateSaveButtonState()

            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showFrequencyBottomSheet() {
        val dialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        val view = LayoutInflater.from(this).inflate(R.layout.layout_bottom_sheet_frequency, null)
        dialog.setContentView(view)

        val doneButton = view.findViewById<TextView>(R.id.btn_done)
        val frequencyList = view.findViewById<LinearLayout>(R.id.frequency_list)
        val mainText = findViewById<TextView>(R.id.frequency)

        var selectedItem: View? = null
        var selectedFrequency: Frequency? = this.selectedFrequency

        SampleData.frequencies.forEachIndexed { index, frequency ->
            val itemView = LayoutInflater.from(this)
                .inflate(R.layout.item_frequency, frequencyList, false)

            val nameView = itemView.findViewById<TextView>(R.id.frequency_name)
            val checkIcon = itemView.findViewById<ImageView>(R.id.check_icon)
            nameView.text = frequency.name

            if (this.selectedFrequency != null && this.selectedFrequency?.name == frequency.name) {
                checkIcon.visibility = View.VISIBLE
                selectedItem = itemView
            }

            itemView.setOnClickListener {
                selectedItem?.findViewById<ImageView>(R.id.check_icon)?.visibility = View.GONE
                checkIcon.visibility = View.VISIBLE
                selectedItem = itemView
                selectedFrequency = frequency
            }

            frequencyList.addView(itemView)

            if (index < SampleData.frequencies.size - 1) {
                val divider = View(this)
                divider.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    (1 * resources.displayMetrics.density).toInt()
                )
                divider.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray))
                frequencyList.addView(divider)
            }
        }

        doneButton.setOnClickListener {
            selectedFrequency?.let { freq ->
                mainText.text = freq.name
                this.selectedFrequency = freq
            }

            updateSaveButtonState()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showCategoryBottomSheet() {
        val dialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        val view = LayoutInflater.from(this).inflate(R.layout.layout_bottom_sheet_category, null)
        dialog.setContentView(view)

        val doneButton = view.findViewById<TextView>(R.id.btn_done)
        val categoryList = view.findViewById<LinearLayout>(R.id.category_list)

        val mainText = findViewById<TextView>(R.id.subscription)
        val mainIcon = findViewById<ImageView>(R.id.subsImg)

        var selectedItem: View? = null
        var selectedCategory: Category? = this.selectedCategory

        SampleData.categories.forEachIndexed { index, category ->
            val itemView = LayoutInflater.from(this)
                .inflate(R.layout.item_category, categoryList, false)

            val nameView = itemView.findViewById<TextView>(R.id.category_name)
            val checkCircle = itemView.findViewById<View>(R.id.category_circle)
            val iconView = itemView.findViewById<ImageView>(R.id.category_icon)

            nameView.text = category.name
            iconView.setImageResource(category.iconRes)

            if (this.selectedCategory != null && this.selectedCategory?.name == category.name) {
                checkCircle.visibility = View.VISIBLE
                selectedItem = itemView
            }

            itemView.setOnClickListener {
                selectedItem?.findViewById<View>(R.id.category_circle)?.visibility = View.GONE
                checkCircle.visibility = View.VISIBLE
                selectedItem = itemView
                selectedCategory = category
            }

            categoryList.addView(itemView)

            if (index < SampleData.categories.size - 1) {
                val divider = View(this)
                divider.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    (1 * resources.displayMetrics.density).toInt()
                )
                divider.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray))
                categoryList.addView(divider)
            }
        }

        doneButton.setOnClickListener {
            selectedCategory?.let { category ->
                mainText.text = category.name
                mainIcon.setImageResource(category.iconRes)
                mainIcon.background = null
                mainIcon.imageTintMode = null

                this.selectedCategory = category
            }
            updateSaveButtonState()
            dialog.dismiss()
        }

        dialog.show()
    }

}
