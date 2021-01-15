package com.example.kaffeineme.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.kaffeineme.R
import com.example.kaffeineme.data.classes.KaffeineViewModel
import com.example.kaffeineme.data.shared_preference.SaveData
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {

    private lateinit var saveData: SaveData
    private lateinit var mKaffeineViewModel: KaffeineViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        // Apply the Theme
        setUpTheme()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        mKaffeineViewModel = ViewModelProvider(this).get(KaffeineViewModel::class.java)

        if (saveData.loadDarkState()) { // if true..
            switch_button.isChecked = true
        }

        switch_button.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                saveData.setDarkModeState(true)
                restartApplication()
            } else {
                saveData.setDarkModeState(false)
                restartApplication()
            }
        }

        update_user.setOnClickListener {
            updateAction()
        }

    }

    private fun updateAction() {
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
            intent.putExtra("activity", 1)

            startActivity(intent)
        })
    }

    private fun restartApplication() {
        val intent = Intent(applicationContext, SettingActivity::class.java)
        startActivity(intent)
        finish()
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
}