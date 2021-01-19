package com.example.kaffeineme.activities

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.kaffeineme.R
import com.example.kaffeineme.data.classes.KaffeineViewModel
import com.example.kaffeineme.data.classes.User
import com.example.kaffeineme.data.shared_preference.SaveData
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*
import kotlin.collections.ArrayList

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION")
class RegisterActivity : AppCompatActivity() {

    private lateinit var saveData: SaveData
    private lateinit var mKaffeineViewModel: KaffeineViewModel

    private var item = ArrayList<String>()
    private var mActivity = 0
    private var mUpdate = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        // Apply the Theme
        setUpTheme()
        // Apply the Theme
        if (saveData.loadArabicLanguageState() == 1) {
            setUpLanguage("ar")
        } else setUpLanguage("en")

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_register)

        mKaffeineViewModel = ViewModelProvider(this).get(KaffeineViewModel::class.java)

        setUpIfInsertOrUpdate()
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
    }

    private fun setUpIfInsertOrUpdate() {
        val intent = intent
        mActivity = intent.getIntExtra("activity", mActivity)
        mUpdate = intent.getIntExtra("update", mUpdate)

        if (mActivity == 1 && mUpdate == 0) {
            item = intent.getStringArrayListExtra("current")
            // This is for Update User
            register_title.text = getString(R.string.update_to_continue_text)
            save_or_update.text = getString(R.string.update_text)

            first_name.setText(item[0])
            last_name.setText(item[1])
            email.setText(item[2])
            location.setText(item[3])
            password.setText(item[4])

            save_password.isChecked = saveData.loadSavePasswordState()

            save_or_update.setOnClickListener {
                if (save_password.isChecked) {
                    saveData.setSavePasswordState(true)
                } else {
                    saveData.setSavePasswordState(false)
                }

                updateData()
            }
        }

        if (mUpdate == 1 && mActivity == 0) {
            // Apply the Save Password logic
            savePassword()
            item = intent.getStringArrayListExtra("current")
            // This is for User Sign-In
            register_title.text = getString(R.string.sign_in_to_continue_text)
            save_or_update.text = getString(R.string.sign_in_text)

            email.setText(item[2])
            first_name.visibility = View.GONE
            last_name.visibility = View.GONE
            location.visibility = View.GONE

            save_or_update.setOnClickListener {
                if (save_password.isChecked) {
                    saveData.setSavePasswordState(true)
                } else {
                    saveData.setSavePasswordState(false)
                }

                signInUser()
            }
        }

        if (mActivity == 0 && mUpdate == 0) {
            register_title.text = getString(R.string.sign_up_to_continue_register)
            save_or_update.text = getString(R.string.sign_up_text)
            save_password.visibility = View.GONE

            save_or_update.setOnClickListener {
                insertData()
            }
        }
    }

    private fun signInUser() {
        val mEmail = item[2]
        val mPassword = item[4]
        val sEmail = email.text.toString()
        val sPassword = password.text

        if (secureInputCheck(sEmail, sPassword)) {
            if (sEmail == mEmail && sPassword.toString() == mPassword) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                val builder = AlertDialog.Builder(this)
                builder.setNeutralButton("Ok") { _, _ -> }
                builder.setTitle("Error")
                builder.setMessage("Invalid User")
                builder.create().show()
            }
        } else {
            Snackbar.make(save_or_update, "Fill out all field!!", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun insertData() {
        val mFirstName = first_name.text.toString()
        val mLastName = last_name.text.toString()
        val mEmail = email.text.toString()
        val mLocation = location.text.toString()
        val mPassword = password.text

        if (inputCheck(mFirstName, mLastName, mEmail, mLocation, mPassword)) {

            // Insert User Data
            val user =
                User(
                    1,
                    mFirstName,
                    mLastName,
                    mEmail,
                    mLocation,
                    mPassword.toString()
                )
            mKaffeineViewModel.insertUser(user)

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            Snackbar.make(save_or_update, "Fill out all field!!", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun updateData() {
        val mFirstName = first_name.text.toString()
        val mLastName = last_name.text.toString()
        val mEmail = email.text.toString()
        val mLocation = location.text.toString()
        val mPassword = password.text

        if (inputCheck(mFirstName, mLastName, mEmail, mLocation, mPassword)) {

            // Update User Data
            val user =
                User(
                    1,
                    mFirstName,
                    mLastName,
                    mEmail,
                    mLocation,
                    mPassword.toString()
                )
            mKaffeineViewModel.updateUser(user)

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            Snackbar.make(save_or_update, "Fill out all field!!", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun savePassword() {
        // share preference
        saveData = SaveData(this)
        if (saveData.loadSavePasswordState()) { // if true..
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
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

    private fun inputCheck(
        mFirstName: String,
        mLastName: String,
        mEmail: String,
        mLocation: String,
        mPassword: Editable?
    ): Boolean {
        return !(TextUtils.isEmpty(mFirstName)) &&
                !(TextUtils.isEmpty(mLastName)) &&
                !(TextUtils.isEmpty(mEmail)) &&
                !(TextUtils.isEmpty(mLocation)) &&
                !(TextUtils.isEmpty(mPassword))
    }

    private fun secureInputCheck(sEmail: String, sPassword: Editable?): Boolean {
        return !(TextUtils.isEmpty(sEmail)) &&
                !(TextUtils.isEmpty(sPassword))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
