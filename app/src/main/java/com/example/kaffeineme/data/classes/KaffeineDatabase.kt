package com.example.kaffeineme.data.classes

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.kaffeineme.data.interfaces.KaffeineDao

@Suppress("NAME_SHADOWING")
@Database(entities = [Kaffeine::class, User::class], version = 1, exportSchema = false)
abstract class KaffeineDatabase : RoomDatabase() {

    abstract fun kaffeineDao(): KaffeineDao

    companion object {
        //prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: KaffeineDatabase? = null

        fun getDatabase(context: Context): KaffeineDatabase {

            val instance = INSTANCE
            // if the INSTANCE is not null, then return it
            if (instance != null) {
                return instance
            }
            // if it is null, then create the database
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KaffeineDatabase::class.java,
                    "kaffeine_database"
                )
                    .fallbackToDestructiveMigration() // Wipes and rebuilds instead of migrating if no Migration object.
                    .build()
                INSTANCE = instance

                return instance
            }
        }
    }
}
