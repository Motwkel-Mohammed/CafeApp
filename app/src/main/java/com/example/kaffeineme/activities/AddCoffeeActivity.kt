package com.example.kaffeineme.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.kaffeineme.R
import com.example.kaffeineme.data.classes.Kaffeine
import com.example.kaffeineme.data.classes.KaffeineViewModel
import com.example.kaffeineme.data.shared_preference.SaveData
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_add_coffee.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION")
class AddCoffeeActivity : AppCompatActivity() {

    private lateinit var saveData: SaveData
    private lateinit var mKaffeineViewModel: KaffeineViewModel
    private var item = ArrayList<String>()
    private var mActivity = 0

    @SuppressLint("SetTextI18n", "WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        // Apply the Theme
        setUpTheme()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_coffee)

        mKaffeineViewModel = ViewModelProvider(this).get(KaffeineViewModel::class.java)

        setUpIfInsertOrUpdate()
    }

    private fun setUpIfInsertOrUpdate() {
        val intent = intent
        mActivity = intent.getIntExtra("activity", mActivity)

        if (mActivity == 1) {
            item = intent.getStringArrayListExtra("current")
        }

        if (item.isEmpty()) {
            add_activity_title.text = "Insert"
            save_or_update.text = "Save"

            save_or_update.setOnClickListener {
                insertData()
            }
        } else {
            add_activity_title.text = "Update"
            save_or_update.text = "Update"

            coffee_name.setText(item[1])
            coffee_description.setText(item[2])
            coffee_price.setText(makeDouble(item[3]))

            save_or_update.setOnClickListener {
                updateData()
            }
        }
    }

    private fun insertData() {
        val mCoffeeName = coffee_name.text.toString()
        val mCoffeeDescription = coffee_description.text.toString()
        val mCoffeePrice = coffee_price.text

        if (inputCheck(mCoffeeName, mCoffeeDescription, mCoffeePrice)) {
            // Insert Coffee Data
            val kaffeine =
                Kaffeine(
                    0,
                    mCoffeeName,
                    mCoffeeDescription,
                    mCoffeePrice.toString().toDouble(),
                    randomImage()
                )
            mKaffeineViewModel.insertCoffee(kaffeine)

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            Snackbar.make(save_or_update, "Fill out all field!!", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun updateData() {
        val mCoffeeName = coffee_name.text.toString()
        val mCoffeeDescription = coffee_description.text.toString()
        val mCoffeePrice = coffee_price.text
        val mCoffeeImage = item[4].toInt()
        val id = item[0].toLong()

        if (inputCheck(mCoffeeName, mCoffeeDescription, mCoffeePrice)) {

            // Update Coffee Data
            val kaffeine =
                Kaffeine(
                    id,
                    mCoffeeName,
                    mCoffeeDescription,
                    mCoffeePrice.toString().toDouble(),
                    mCoffeeImage
                )
            mKaffeineViewModel.updateCoffee(kaffeine)

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            Snackbar.make(save_or_update, "Fill out all field!!", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun setUpTheme() {
        // share preference
        saveData = SaveData(this)
        if (saveData.loadDarkState()) { // if true..
            setTheme(R.style.DarkTheme)
        } else {
            setTheme(R.style.AppTheme)
        }
    }

    private fun makeDouble(mCoffeePrice: String?): String {
        val sCoffeePrice = mCoffeePrice!!.toDouble()
        return when (sCoffeePrice) {
            in 1..9 -> String.format("%.2f", sCoffeePrice)
            else -> sCoffeePrice.toString()
        }
    }

    private fun inputCheck(
        mCoffeeName: String,
        mCoffeeDescription: String,
        mCoffeePrice: Editable?
    ): Boolean {
        return !(TextUtils.isEmpty(mCoffeeName)) &&
                !(TextUtils.isEmpty(mCoffeeDescription)) &&
                !(TextUtils.isEmpty(mCoffeePrice))
    }

    private fun randomImage(): Int {
        return (1..4).random()
    }

}
