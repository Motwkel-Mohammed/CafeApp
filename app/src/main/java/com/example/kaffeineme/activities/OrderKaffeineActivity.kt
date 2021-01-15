package com.example.kaffeineme.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.kaffeineme.R
import com.example.kaffeineme.data.classes.KaffeineViewModel
import com.example.kaffeineme.data.shared_preference.SaveData
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_order_kaffeine.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "SetTextI18n", "DEPRECATION")
class OrderKaffeineActivity : AppCompatActivity() {

    private lateinit var saveData: SaveData
    private lateinit var mKaffeineViewModel: KaffeineViewModel

    private var item = ArrayList<String>()
    private var mQuantityNumber = 1
    private var mCoffeeName = ""
    private var mCoffeeDescription = ""
    private var mCoffeePrice = 0.0
    private var sCoffeePrice = 0.0
    private var mCoffeeImage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        // Apply the Theme
        setUpTheme()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_kaffeine)

        mKaffeineViewModel = ViewModelProvider(this).get(KaffeineViewModel::class.java)

        // Display our selected coffee data
        displayData()

        quantity_minus.setOnClickListener {
            // Decrease number of coffee
            decreaseCoffee()
        }

        quantity_plus.setOnClickListener {
            // Increase number of coffee
            increaseCoffee()
        }

        order_button.setOnClickListener {
            orderAction()
        }
    }

    private fun orderAction() {
        // Display User Data
        mKaffeineViewModel.allUser.observe(this, Observer { user ->
            val current = user[0]

            val mFirstName = current.userFirstName
            val mLastName = current.userLastName

            val mItem = mutableListOf<String>()

            mItem.add(mFirstName)
            mItem.add(mLastName)
            // Return the order Coffee data in List
            val orderList: List<String> = orderCoffee(mItem)
            // Send Email
            sendEmail(orderList[0], orderList[1])
        })
    }

    private fun decreaseCoffee() {
        if (mQuantityNumber == 1) {
            Snackbar.make(quantity_plus, "You must order some coffee", Snackbar.LENGTH_SHORT).show()
        } else {
            mQuantityNumber -= 1
            quantity_number.text = mQuantityNumber.toString()
        }
    }

    private fun increaseCoffee() {
        if (mQuantityNumber == 10) {
            Snackbar.make(quantity_plus, "Can't order more than 10 Coffee", Snackbar.LENGTH_SHORT)
                .show()
        } else {
            mQuantityNumber += 1
            quantity_number.text = mQuantityNumber.toString()
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

    private fun displayData() {
        val intent = intent
        item = intent.getStringArrayListExtra("current")

        mCoffeeName = item[1]
        mCoffeeDescription = item[2]
        mCoffeePrice = item[3].toDouble()
        mCoffeeImage = item[4].toInt()

        coffee_name.text = mCoffeeName
        coffee_description.text = mCoffeeDescription
        coffee_price.text = "$${makeDouble(mCoffeePrice)}"
        coffee_image.load(image(mCoffeeImage)) {
            crossfade(true)
            crossfade(1000)
        }
    }

    private fun sendEmail(subject: String, message: String) {
        val mIntent = Intent(Intent.ACTION_SEND)
        mIntent.data = Uri.parse("mailto:")
        mIntent.type = "text/plain"
        // Put Email Info to Send
        //mIntent.putExtra(Intent.EXTRA_EMAIL, "kooli2015@gmail.com")
        mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        mIntent.putExtra(Intent.EXTRA_TEXT, message)

        try {
            startActivity(Intent.createChooser(mIntent, "Choose Email Client.."))
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }

        startActivity(Intent.createChooser(mIntent, "Choose an Email client :"))
    }

    private fun orderCoffee(item: List<String>): List<String> {
        sCoffeePrice = mCoffeePrice
        mCoffeePrice *= mQuantityNumber

        val subject = "Order from ${item[0]} ${item[1]}"

        val message = """
                |Coffee Name: $mCoffeeName 
                |Coffee Quantity: $mQuantityNumber
                |Coffee Price: $${String.format("%.3f", sCoffeePrice).toDouble()}
                |Total = $${String.format("%.3f", mCoffeePrice).toDouble()} 
                |Thank you for ordering from Kaffeine app!!""".trimMargin()

        val mItem = mutableListOf<String>()

        mItem.add(subject)
        mItem.add(message)
        return mItem
    }

    private fun makeDouble(mCoffeePrice: Double?): String {
        return when (val sCoffeePrice = mCoffeePrice!!.toDouble()) {
            in 1..9 -> String.format("%.2f", sCoffeePrice)
            else -> sCoffeePrice.toString()
        }
    }

    private fun image(coffeeImage: Int): Int {
        return when (coffeeImage) {
            1 -> R.drawable.cafe_latte
            2 -> R.drawable.cappuccino
            3 -> R.drawable.filter_coffee
            4 -> R.drawable.mochaccino
            else -> R.drawable.cappuccino
        }
    }
}
