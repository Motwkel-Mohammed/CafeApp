package com.example.kaffeineme.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.kaffeineme.R
import com.example.kaffeineme.data.classes.KaffeineViewModel

class SplashActivity : AppCompatActivity() {

    private lateinit var mKaffeineViewModel: KaffeineViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        mKaffeineViewModel = ViewModelProvider(this).get(KaffeineViewModel::class.java)

        Handler().postDelayed({
            setUpIfRegisterOrMain()
        }, 3000)
    }

    private fun setUpIfRegisterOrMain() {
        // Check if list isEmpty or not
        mKaffeineViewModel.allUser.observe(this, Observer { user ->
            if (user.isEmpty()) {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, RegisterActivity::class.java)

                val item = ArrayList<String>()
                //  Get User number 0
                mKaffeineViewModel.allUser.observe(this, Observer { user ->
                    val current = user[0]

                    item.add(current.userFirstName)
                    item.add(current.userLastName)
                    item.add(current.userEmail)
                    item.add(current.userLocation)
                    item.add(current.userPassword)

                    intent.putStringArrayListExtra("current", item)
                    intent.putExtra("update", 1)

                    startActivity(intent)
                    finish()
                })
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        startActivity(intent)
    }
}
