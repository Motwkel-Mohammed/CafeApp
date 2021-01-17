package com.example.kaffeineme.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kaffeineme.R
import com.example.kaffeineme.data.classes.HorizontalRecyclerAdapter
import com.example.kaffeineme.data.classes.Kaffeine
import com.example.kaffeineme.data.classes.KaffeineViewModel
import com.example.kaffeineme.data.classes.VerticalRecyclerAdapter
import com.example.kaffeineme.data.shared_preference.SaveData
import kotlinx.android.synthetic.main.activity_main.*

@SuppressLint("WrongConstant")
class MainActivity : AppCompatActivity(), HorizontalRecyclerAdapter.RowClickListener,
    VerticalRecyclerAdapter.RowClickListener {

    private lateinit var saveData: SaveData
    private lateinit var verticalAdapter: VerticalRecyclerAdapter
    private lateinit var horizontalAdapter: HorizontalRecyclerAdapter
    private lateinit var mKaffeineViewModel: KaffeineViewModel

    private lateinit var itemTouchHelperCallback: ItemTouchHelper.SimpleCallback
    private var deleteSwipeBackground: ColorDrawable = ColorDrawable(Color.parseColor("#E9633D"))
    private var editSwipeBackground: ColorDrawable = ColorDrawable(Color.parseColor("#0F9D58"))
    private lateinit var deleteSwipeIcon: Drawable
    private lateinit var editSwipeIcon: Drawable

    @SuppressLint("SetTextI18n", "WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        // Apply the Theme
        setUpTheme()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mKaffeineViewModel = ViewModelProvider(this).get(KaffeineViewModel::class.java)

        deleteSwipeIcon = ContextCompat.getDrawable(this, R.drawable.ic_delete_white)!!
        editSwipeIcon = ContextCompat.getDrawable(this, R.drawable.ic_edit_white)!!

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
        setTouchCallback()
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(vertical_recycler_view)
    }

    private fun setTouchCallback() {
        itemTouchHelperCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        verticalAdapter.updateItem(viewHolder, this@MainActivity)
                    }
                    ItemTouchHelper.RIGHT -> {
                        verticalAdapter.removeItem(viewHolder, this@MainActivity)
                    }
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val deleteIconMargin = (itemView.height - deleteSwipeIcon.intrinsicHeight) / 2
                val editIconMargin = (itemView.height - editSwipeIcon.intrinsicHeight) / 2

                if (dX > 0) {
                    deleteSwipeBackground.setBounds(
                        itemView.left,
                        itemView.top,
                        dX.toInt(),
                        itemView.bottom
                    )
                    deleteSwipeIcon.setBounds(
                        itemView.left + editIconMargin,
                        itemView.top + editIconMargin,
                        itemView.left + editIconMargin + editSwipeIcon.intrinsicWidth,
                        itemView.bottom - editIconMargin
                    )
                } else {
                    editSwipeBackground.setBounds(
                        itemView.right + dX.toInt(),
                        itemView.top,
                        itemView.right,
                        itemView.bottom
                    )
                    editSwipeIcon.setBounds(
                        itemView.right - deleteIconMargin - deleteSwipeIcon.intrinsicWidth,
                        itemView.top + deleteIconMargin,
                        itemView.right - deleteIconMargin,
                        itemView.bottom - deleteIconMargin
                    )
                }

                if (dX > 0) {
                    deleteSwipeBackground.draw(c)
                } else {
                    editSwipeBackground.draw(c)
                }

                c.save()

                if (dX > 0) {
                    c.clipRect(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
                } else {
                    c.clipRect(
                        itemView.right + dX.toInt(),
                        itemView.top,
                        itemView.right,
                        itemView.bottom
                    )
                }

                if (dX > 0) {
                    deleteSwipeIcon.draw(c)
                } else {
                    editSwipeIcon.draw(c)
                }

                c.restore()

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
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
        mKaffeineViewModel.allCoffee.observe(this, Observer { kaffeine ->
            verticalAdapter = VerticalRecyclerAdapter((kaffeine) as MutableList<Kaffeine>, this)
            vertical_recycler_view.adapter = verticalAdapter
        })
    }

    private fun setUpHorizontalRecycleView() {
        horizontal_recycler_view.layoutManager =
            LinearLayoutManager(this, LinearLayout.HORIZONTAL, false)
        mKaffeineViewModel.allCoffee.observe(this, Observer { kaffeine ->
            horizontalAdapter = HorizontalRecyclerAdapter((kaffeine) as MutableList<Kaffeine>, this)
            horizontal_recycler_view.adapter = horizontalAdapter
        })
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
