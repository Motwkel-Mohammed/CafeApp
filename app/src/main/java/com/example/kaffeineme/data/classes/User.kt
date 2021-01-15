package com.example.kaffeineme.data.classes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "first_name")
    val userFirstName: String,
    @ColumnInfo(name = "last_name")
    val userLastName: String,
    @ColumnInfo(name = "password")
    val userPassword: String
)