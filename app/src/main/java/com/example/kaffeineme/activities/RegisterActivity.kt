package com.example.kaffeineme.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.kaffeineme.R
import com.example.kaffeineme.data.classes.KaffeineViewModel
import com.example.kaffeineme.data.classes.User
import com.example.kaffeineme.data.shared_preference.SaveData
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var saveData: SaveData
    private lateinit var mKaffeineViewModel: KaffeineViewModel

    private var item = ArrayList<String>()
    private var mActivity = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        // Apply the Theme
        setUpTheme()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

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
            register_title.text = "Insert User"
            save_or_update.text = "Save"

            save_or_update.setOnClickListener {
                insertData()
            }
        } else {
            register_title.text = "Update User"
            save_or_update.text = "Update"

            first_name.setText(item[0])
            last_name.setText(item[1])
            password.setText(item[2])

            save_or_update.setOnClickListener {
                updateData()
            }
        }
    }

    private fun insertData() {
        val mFirstName = first_name.text.toString()
        val mLastName = last_name.text.toString()
        val mPassword = password.text

        if (inputCheck(mFirstName, mLastName, mPassword)) {

            // Insert User Data
            val user =
                User(
                    1,
                    mFirstName,
                    mLastName,
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
        val mPassword = password.text

        if (inputCheck(mFirstName, mLastName, mPassword)) {

            // Update User Data
            val user =
                User(
                    1,
                    mFirstName,
                    mLastName,
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
        mPassword: Editable?
    ): Boolean {
        return !(TextUtils.isEmpty(mFirstName)) &&
                !(TextUtils.isEmpty(mLastName)) &&
                !(TextUtils.isEmpty(mPassword))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        startActivity(intent)
    }
}
