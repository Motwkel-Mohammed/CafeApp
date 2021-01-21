package com.example.kaffeineme.data.classes

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.kaffeineme.R
import com.example.kaffeineme.activities.AddCoffeeActivity
import com.example.kaffeineme.activities.MainActivity
import com.google.android.material.snackbar.Snackbar

@Suppress("DEPRECATION")
@SuppressLint("SetTextI18n")
class VerticalRecyclerAdapter(
    private var items: MutableList<Kaffeine>,
    private val listener: RowClickListener
) :
    RecyclerView.Adapter<VerticalRecyclerAdapter.KaffeineViewHolder>() {

    private lateinit var mKaffeineViewModel: KaffeineViewModel
    private var remove = 0

    class KaffeineViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val coffeeName: TextView = itemView.findViewById(R.id.display_coffee_name)
        val coffeeDescription: TextView = itemView.findViewById(R.id.display_coffee_description)
        val coffeePrice: TextView = itemView.findViewById(R.id.display_coffee_price)
        val coffeeImage: ImageView = itemView.findViewById(R.id.coffee_image)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KaffeineViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.display_vertical_item, parent, false)
        return KaffeineViewHolder(view)
    }

    override fun onBindViewHolder(holder: KaffeineViewHolder, position: Int) {
        val current = items[position]

        holder.coffeeName.text = current.coffeeName
        holder.coffeeDescription.text = current.coffeeDescription
        holder.coffeePrice.text = "$${makeDouble(current.coffeePrice)}"
        holder.coffeeImage.load(image(current.coffeeImage)) {
            crossfade(true)
            crossfade(1000)
            transformations(RoundedCornersTransformation(30f))
        }

        //onItemClickListener
        holder.itemView.setOnClickListener {
            listener.onItemClickListener(current)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setData(kaffeine: List<Kaffeine>) {
        this.items = (kaffeine) as MutableList<Kaffeine>
        notifyDataSetChanged()
    }

    private fun makeDouble(mCoffeePrice: Double?): String {
        return when (val sCoffeePrice = mCoffeePrice!!.toDouble()) {
            in 1..9 -> "%.2f".format(sCoffeePrice)
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

    fun removeItem(viewHolder: RecyclerView.ViewHolder, context: MainActivity) {
        val removedPosition = viewHolder.adapterPosition
        val currentItem = items[removedPosition]

        items.removeAt(removedPosition)
        notifyItemRemoved(removedPosition)

        Snackbar.make(
            viewHolder.itemView,
            "${currentItem.coffeeName} ${context.getString(R.string.delete_icon)}?",
            Snackbar.LENGTH_LONG
        ).setAction(context.getString(R.string.undo_text)) {
            remove = 1
            items.add(removedPosition, currentItem)
            notifyItemInserted(removedPosition)
        }.show()

        Handler().postDelayed({
            if (remove == 0) {
                mKaffeineViewModel = ViewModelProvider(context).get(KaffeineViewModel::class.java)
                mKaffeineViewModel.deleteCoffee(currentItem)
            }
        }, 3000)
    }

    fun updateItem(viewHolder: RecyclerView.ViewHolder, context: Context) {
        val position = viewHolder.adapterPosition
        val current = items[position]

        val intent = Intent(context, AddCoffeeActivity::class.java)

        val item = ArrayList<String>()
        item.add(current.id.toString())
        item.add(current.coffeeName)
        item.add(current.coffeeDescription)
        item.add(current.coffeePrice.toString())
        item.add(current.coffeeImage.toString())

        intent.putStringArrayListExtra("current", item)
        intent.putExtra("activity", 1)
        context.startActivity(intent)
    }

    interface RowClickListener {
        fun onItemClickListener(kaffeine: Kaffeine)
    }
}