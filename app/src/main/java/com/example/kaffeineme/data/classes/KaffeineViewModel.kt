package com.example.kaffeineme.data.classes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class KaffeineViewModel(application: Application) : AndroidViewModel(application) {

    val allCoffee: LiveData<List<Kaffeine>>
    val allUser: LiveData<List<User>>
    val repository: KaffeineRepository

    init {
        var kaffeineDao = KaffeineDatabase.getDatabase(application).kaffeineDao()
        repository = KaffeineRepository(kaffeineDao)
        // Kaffeine table
        allCoffee = repository.allCoffee
        // User table
        allUser = repository.allUser
    }

    // Kaffeine table
    fun insertCoffee(kaffeine: Kaffeine) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertCoffee(kaffeine)
    }

    fun updateCoffee(kaffeine: Kaffeine) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateCoffee(kaffeine)
    }

    fun deleteCoffee(kaffeine: Kaffeine) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteCoffee(kaffeine)
    }

    fun deleteAllCoffee() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAllCoffee()
    }

    // User table
    fun insertUser(user: User) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertUser(user)
    }

    fun updateUser(user: User) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateUser(user)
    }

    fun deleteUser(user: User) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteUser(user)
    }
}
