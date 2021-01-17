package com.example.kaffeineme.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.kaffeineme.R
import com.example.kaffeineme.data.classes.KaffeineViewModel
import com.example.kaffeineme.data.classes.User
import com.example.kaffeineme.data.shared_preference.SaveData
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_register.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class RegisterActivity : AppCompatActivity() {

    private lateinit var saveData: SaveData
    private lateinit var mKaffeineViewModel: KaffeineViewModel

    private var item = ArrayList<String>()
    private var mActivity = 0
    private var mUpdate = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        // Apply the Theme
        setUpTheme()
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

    private fun setUpIfInsertOrUpdate() {
        val intent = intent
        mActivity = intent.getIntExtra("activity", mActivity)
        mUpdate = intent.getIntExtra("update", mUpdate)

        if (mActivity == 1 && mUpdate == 0) {
            item = intent.getStringArrayListExtra("current")
            // This is for Update User
            register_title.text = "Update to Continue"
            save_or_update.text = "Update"

            first_name.setText(item[0])
            last_name.setText(item[1])
            email.setText(item[2])
            location.setText(item[3])
            password.setText(item[4])

            save_or_update.setOnClickListener {
                updateData()
            }
        }

        if (mUpdate == 1 && mActivity == 0) {
            item = intent.getStringArrayListExtra("current")
            // This is for User Sign-In
            register_title.text = "Sign In to Continue"
            save_or_update.text = "Sign In"

            first_name.visibility = View.GONE
            last_name.visibility = View.GONE
            location.visibility = View.GONE

            save_or_update.setOnClickListener {
                signInUser()
            }
        }

        if (mActivity == 0 && mUpdate == 0) {
            register_title.text = "Sign Up to Continue"
            save_or_update.text = "Sign Up"

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
