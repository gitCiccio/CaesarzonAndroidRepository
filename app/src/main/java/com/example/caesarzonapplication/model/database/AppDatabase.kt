package com.example.caesarzonapplication.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.caesarzonapplication.model.entities.Follower

@Database (entities = [Follower::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase()  {

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "follower_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}