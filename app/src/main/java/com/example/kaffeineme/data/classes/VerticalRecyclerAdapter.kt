package com.example.kaffeineme.data.classes

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.kaffeineme.R

@Suppress("DEPRECATION")
@SuppressLint("SetTextI18n")
class VerticalRecyclerAdapter(val listener: RowClickListener) :
    RecyclerView.Adapter<VerticalRecyclerAdapter.KaffeineViewHolder>() {

    private var kaffeineList = emptyList<Kaffeine>()

    class KaffeineViewHolder(itemView: View, val listener: RowClickListener) :
        RecyclerView.ViewHolder(itemView) {
        val coffeeName = itemView.findViewById<TextView>(R.id.display_coffee_name)
        val coffeeDescription = itemView.findViewById<TextView>(R.id.display_coffee_description)
        val coffeePrice = itemView.findViewById<TextView>(R.id.display_coffee_price)
        val deleteCoffee = itemView.findViewById<ImageView>(R.id.delete_coffee)
        val coffeeImage = itemView.findViewById<ImageView>(R.id.coffee_image)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KaffeineViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.display_vertical_item, parent, false)
        return KaffeineViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: KaffeineViewHolder, position: Int) {
        val current = kaffeineList[position]

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
        val sCoffeePrice = mCoffeePrice!!.toDouble()
        return when (sCoffeePrice) {
            in 1..9 -> String.format("%.2f", sCoffeePrice)
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

    interface RowClickListener {
        fun onDeleteClickListener(kaffeine: Kaffeine)
        fun onItemClickListener(kaffeine: Kaffeine)
        fun onItemLongClickListener(kaffeine: Kaffeine): Boolean
    }

}