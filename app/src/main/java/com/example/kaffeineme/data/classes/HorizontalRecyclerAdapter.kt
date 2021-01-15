package com.example.kaffeineme.data.classes

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.kaffeineme.R

@Suppress("DEPRECATION")
@SuppressLint("SetTextI18n")
class HorizontalRecyclerAdapter(private val listener: RowClickListener) :
    RecyclerView.Adapter<HorizontalRecyclerAdapter.KaffeineViewHolder>() {

    private var kaffeineList = emptyList<Kaffeine>()

    class KaffeineViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val coffeeName: TextView = itemView.findViewById(R.id.display_coffee_name)
        val coffeeDescription: TextView = itemView.findViewById(R.id.display_coffee_description)
        val coffeePrice: TextView = itemView.findViewById(R.id.display_coffee_price)
        val deleteCoffee: ImageView = itemView.findViewById(R.id.delete_coffee)
        val coffeeImage: ImageView = itemView.findViewById(R.id.coffee_image)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KaffeineViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.display_horizontal_item, parent, false)
        return KaffeineViewHolder(view)
    }

    override fun onBindViewHolder(holder: KaffeineViewHolder, position: Int) {
        val current = kaffeineList[position]

        holder.coffeeName.text = current.coffeeName
        holder.coffeeDescription.text = current.coffeeDescription
        holder.coffeePrice.text = "$${makeDouble(current.coffeePrice)}"
        holder.coffeeImage.load(image(current.coffeeImage)) {
            crossfade(true)
            crossfade(1000)
        }

        //onItemClickListener
        holder.itemView.setOnClickListener {
            listener.onItemClickListener(current)
        }
        //onItemLongClickListener
        holder.itemView.setOnLongClickListener {
            listener.onItemLongClickListener(current)
        }
        //onDeleteClickListener
        holder.deleteCoffee.setOnClickListener {
            listener.onDeleteClickListener(current)
        }
    }

    override fun getItemCount(): Int {
        return kaffeineList.size
    }

    fun setData(kaffeine: List<Kaffeine>) {
        this.kaffeineList = kaffeine
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
            1 -> R.drawable.cafe_latte_1
            2 -> R.drawable.cappuccino_1
            3 -> R.drawable.filter_coffee_1
            4 -> R.drawable.mochaccino_1
            else -> R.drawable.cappuccino_1
        }
    }

    interface RowClickListener {
        fun onDeleteClickListener(kaffeine: Kaffeine)
        fun onItemClickListener(kaffeine: Kaffeine)
        fun onItemLongClickListener(kaffeine: Kaffeine): Boolean
    }

}