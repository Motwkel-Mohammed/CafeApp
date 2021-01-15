package com.example.kaffeineme.data.interfaces

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.kaffeineme.data.classes.Kaffeine
import com.example.kaffeineme.data.classes.User

@Dao
interface KaffeineDao {

    // Kaffeine table
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCoffee(kaffeine: Kaffeine)

    @Update
    suspend fun updateCoffee(kaffeine: Kaffeine)

    @Delete
    suspend fun deleteCoffee(kaffeine: Kaffeine)

    @Query("DELETE FROM kaffeine_table")
    suspend fun deleteAllCoffee()

    @Query("SELECT * FROM kaffeine_table ORDER BY coffee_price ASC")
    fun getAllCoffee(): LiveData<List<Kaffeine>>

    // User table
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM user_table ORDER BY first_name ASC")
    fun getAllUser(): LiveData<List<User>>
}
