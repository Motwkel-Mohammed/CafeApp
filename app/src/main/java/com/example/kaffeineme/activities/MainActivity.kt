package com.example.kaffeineme.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kaffeineme.R
import com.example.kaffeineme.data.classes.HorizontalRecyclerAdapter
import com.example.kaffeineme.data.classes.Kaffeine
import com.example.kaffeineme.data.classes.KaffeineViewModel
import com.example.kaffeineme.data.classes.VerticalRecyclerAdapter
import com.example.kaffeineme.data.shared_preference.SaveData
import kotlinx.android.synthetic.main.activity_main.*

@SuppressLint("WrongConstant")
class MainActivity : AppCompatActivity(), VerticalRecyclerAdapter.RowClickListener,
    HorizontalRecyclerAdapter.RowClickListener {

    private lateinit var saveData: SaveData
    private lateinit var verticalAdapter: VerticalRecyclerAdapter
    private lateinit var horizontalAdapter: HorizontalRecyclerAdapter
    private lateinit var mKaffeineViewModel: KaffeineViewModel

    @SuppressLint("SetTextI18n", "WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        // Apply the Theme
        setUpTheme()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mKaffeineViewModel = ViewModelProvider(this).get(KaffeineViewModel::class.java)

        // Vertical RecyclerView
        setUpVerticalRecycleView()

        // Horizontal RecyclerView
        setUpHorizontalRecycleView()

        //  Display Coffee Data
        mKaffeineViewModel.allCoffee.observe(this, Observer { kaffeine ->
            // updateCoffee vertical and horizontal data
            verticalAdapter.setData(kaffeine)
            horizontalAdapter.setData(kaffeine)
        })

        //  Display User Data
        mKaffeineViewModel.allUser.observe(this, Observer { user ->
            val current = user[0]

            val firstName = current.userFirstName
            val lastName = current.userLastName
            user_name.text = "$firstName, $lastName"
        })

        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, AddCoffeeActivity::class.java)
            startActivity(intent)
        }

        setting.setOnClickListener {
            val intent = Intent(this@MainActivity, SettingActivity::class.java)
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

    private fun setUpVerticalRecycleView() {
        vertical_recycler_view.layoutManager =
            LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        verticalAdapter = VerticalRecyclerAdapter(this)
        vertical_recycler_view.adapter = verticalAdapter
    }

    private fun setUpHorizontalRecycleView() {
        horizontal_recycler_view.layoutManager =
            LinearLayoutManager(this, LinearLayout.HORIZONTAL, false)
        horizontalAdapter = HorizontalRecyclerAdapter(this)
        horizontal_recycler_view.adapter = horizontalAdapter
    }

    override fun onDeleteClickListener(kaffeine: Kaffeine) {
        val builder = AlertDialog.Builder(this)
        builder.setPositiveButton("Yes") { _, _ ->
            // Delete Row Coffee Data
            mKaffeineViewModel.deleteCoffee(kaffeine)
        }
        builder.setNegativeButton("No") { _, _ ->

        }
        builder.setTitle("DELETE ${kaffeine.coffeeName}?")
        builder.setMessage("Are you sure you want to deleteCoffee ${kaffeine.coffeeName}?")
        builder.create().show()
    }

    override fun onItemClickListener(kaffeine: Kaffeine) {
        val intent = Intent(this, OrderKaffeineActivity::class.java)

        val item = ArrayList<String>()
        item.add(kaffeine.id.toString())
        item.add(kaffeine.coffeeName)
        item.add(kaffeine.coffeeDescription)
        item.add(kaffeine.coffeePrice.toString())
        item.add(kaffeine.coffeeImage.toString())

        intent.putStringArrayListExtra("current", item)

        startActivity(intent)
    }

    override fun onItemLongClickListener(kaffeine: Kaffeine): Boolean {
        val intent = Intent(this, AddCoffeeActivity::class.java)

        val item = ArrayList<String>()
        item.add(kaffeine.id.toString())
        item.add(kaffeine.coffeeName)
        item.add(kaffeine.coffeeDescription)
        item.add(kaffeine.coffeePrice.toString())
        item.add(kaffeine.coffeeImage.toString())

        intent.putStringArrayListExtra("current", item)
        intent.putExtra("activity", 1)

        startActivity(intent)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_all -> {
                val builder = AlertDialog.Builder(this)
                builder.setPositiveButton("Yes") { _, _ ->
                    // Delete Coffee AllData
                    mKaffeineViewModel.deleteAllCoffee()
                }
                builder.setNegativeButton("No") { _, _ ->

                }
                builder.setTitle("DELETE all data?")
                builder.setMessage("Are you sure you want to deleteCoffee all data?")
                builder.create().show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        startActivity(intent)
    }
}
