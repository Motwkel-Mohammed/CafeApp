package com.example.kaffeineme.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Window
import android.view.WindowManager
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
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
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
            add_activity_title.text = getString(R.string.add_coffee_add_screen)
            save_or_update.text = getString(R.string.add_coffee_button)

            save_or_update.setOnClickListener {
                insertData()
            }
        } else {
            add_activity_title.text = getString(R.string.edit_coffee_text)
            save_or_update.text = getString(R.string.update_text)

            coffee_name.setText(item[1])
            coffee_description.setText(item[2])
            coffee_price.setText(makeDouble(item[3]))

            save_or_update.setOnClickListener {
                updateData()
            }
        }
    }

    private fun insertData() {
        val mCoffeeName = coffee_name.text.toString().trim()
        val mCoffeeDescription = coffee_description.text.toString().trim()
        val mCoffeePrice = coffee_price.text.toString().trim()

        if (inputCheck(mCoffeeName, mCoffeeDescription, mCoffeePrice)) {
            if (mCoffeePrice.toDouble() > 25.99) {
                Snackbar.make(
                    coffee_price,
                    getString(R.string.expensive_text),
                    Snackbar.LENGTH_LONG
                ).show()
            } else {
                // Insert Coffee Data
                val kaffeine =
                    Kaffeine(
                        0,
                        mCoffeeName,
                        mCoffeeDescription,
                        mCoffeePrice.toDouble(),
                        randomImage()
                    )
                mKaffeineViewModel.insertCoffee(kaffeine)

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        } else {
            Snackbar.make(
                save_or_update,
                getString(R.string.fill_out_all_field_text),
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun updateData() {
        val mCoffeeName = coffee_name.text.toString().trim()
        val mCoffeeDescription = coffee_description.text.toString().trim()
        val mCoffeePrice = coffee_price.text.toString().trim()
        val mCoffeeImage = item[4].toInt()
        val id = item[0].toLong()

        if (inputCheck(mCoffeeName, mCoffeeDescription, mCoffeePrice)) {
            if (mCoffeePrice.toDouble() > 25.99) {
                Snackbar.make(
                    coffee_price,
                    getString(R.string.expensive_text),
                    Snackbar.LENGTH_LONG
                ).show()
            } else {
                // Update Coffee Data
                val kaffeine =
                    Kaffeine(
                        id,
                        mCoffeeName,
                        mCoffeeDescription,
                        mCoffeePrice.toDouble(),
                        mCoffeeImage
                    )
                mKaffeineViewModel.updateCoffee(kaffeine)

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        } else {
            Snackbar.make(
                save_or_update,
                getString(R.string.fill_out_all_field_text),
                Snackbar.LENGTH_LONG
            ).show()
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
        return when (val sCoffeePrice = mCoffeePrice!!.toDouble()) {
            in 1..9 -> String.format("%.2f", sCoffeePrice)
            else -> sCoffeePrice.toString()
        }
    }

    private fun inputCheck(
        mCoffeeName: String,
        mCoffeeDescription: String,
        mCoffeePrice: String
    ): Boolean {
        return !(TextUtils.isEmpty(mCoffeeName)) &&
                !(TextUtils.isEmpty(mCoffeeDescription)) &&
                !(TextUtils.isEmpty(mCoffeePrice))
    }

    private fun randomImage(): Int {
        return (1..4).random()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
