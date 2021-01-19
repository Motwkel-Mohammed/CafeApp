package com.example.kaffeineme.activities

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.kaffeineme.R
import com.example.kaffeineme.data.classes.KaffeineViewModel
import com.example.kaffeineme.data.shared_preference.SaveData
import kotlinx.android.synthetic.main.activity_setting.*
import java.util.*
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
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
            dark_mode_text.text = getString(R.string.dark_text)
        } else {
            dark_mode_text.text = getString(R.string.light_text)
        }
        if (saveData.loadArabicLanguageState() == 1) {
            language_text.text = getString(R.string.arabic_text)
        } else {
            language_text.text = getString(R.string.english_text)
        }

        language.setOnClickListener {
            languageLogic()
        }

        dark_mode.setOnClickListener {
            darkModeLogic()
        }

        update_user.setOnClickListener {
            updateAction()
        }

    }

    private fun darkModeLogic() {
        val item = arrayOf(getString(R.string.dark_text), getString(R.string.light_text))
        val builder = AlertDialog.Builder(this)
        builder.setSingleChoiceItems(item, -1) { dialogInterface, i ->
            if (i == 0) {
                // This is Dark theme
                saveData.setDarkModeState(true)
                restartApplication()
            } else if (i == 1) {
                // This is Light theme
                saveData.setDarkModeState(false)
                restartApplication()
            }
            dark_mode_text.text = item[i]

            dialogInterface.dismiss()
        }
        builder.setNeutralButton(getString(R.string.cancel_text)) { dialog, _ ->
            dialog.cancel()
        }
        builder.setTitle(getString(R.string.choose_theme_text))
        builder.create().show()
    }

    private fun languageLogic() {
        val item = arrayOf("English", "العربية")
        val builder = AlertDialog.Builder(this)
        builder.setSingleChoiceItems(item, -1) { dialogInterface, i ->
            if (i == 0) {
                // This English
                setUpLanguage("en")
            } else if (i == 1) {
                // This Arabic
                setUpLanguage("ar")
            }
            language_text.text = item[i]

            dialogInterface.dismiss()
        }
        builder.setNeutralButton(getString(R.string.cancel_text)) { dialog, _ ->
            dialog.cancel()
        }
        builder.setTitle(getString(R.string.choose_language_text))
        builder.create().show()
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

    private fun setUpLanguage(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        this.resources.updateConfiguration(config, this.resources.displayMetrics)
        if (language == "en") {
            saveData.setArabicLanguageState(0)
        } else if (language == "ar") {
            saveData.setArabicLanguageState(1)
        }
        restartApplication()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}