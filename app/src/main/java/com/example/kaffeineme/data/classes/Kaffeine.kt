package com.example.kaffeineme.data.classes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kaffeine_table")
data class Kaffeine(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "coffee_name")
    val coffeeName: String,
    @ColumnInfo(name = "coffee_description")
    val coffeeDescription: String,
    @ColumnInfo(name = "coffee_price")
    val coffeePrice: Double,
    @ColumnInfo(name = "coffee_image")
    val coffeeImage: Int
)
