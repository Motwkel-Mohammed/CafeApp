package com.example.kaffeineme.data.classes

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.kaffeineme.data.interfaces.KaffeineDao

class KaffeineRepository(private val kaffeineDao: KaffeineDao) {

    // Kaffeine table
    val allCoffee: LiveData<List<Kaffeine>> = kaffeineDao.getAllCoffee()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertCoffee(kaffeine: Kaffeine) {
        kaffeineDao.insertCoffee(kaffeine)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateCoffee(kaffeine: Kaffeine) {
        kaffeineDao.updateCoffee(kaffeine)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteCoffee(kaffeine: Kaffeine) {
        kaffeineDao.deleteCoffee(kaffeine)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAllCoffee() {
        kaffeineDao.deleteAllCoffee()
    }

    // User table
    val allUser: LiveData<List<User>> = kaffeineDao.getAllUser()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertUser(user: User) {
        kaffeineDao.insertUser(user)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateUser(user: User) {
        kaffeineDao.updateUser(user)
    }

    /*
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteUser(user: User) {
        kaffeineDao.deleteUser(user)
    }

     */
}