package com.filipe1309.abasteceai.libraries.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.filipe1309.abasteceai.libraries.database.model.Fuel
import com.filipe1309.abasteceai.libraries.database.model.History


const val DATABASE_NAME = "abasteceai_database.db"

@Database(entities = [Fuel::class, History::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract val fuelDAO : FuelDAO
    abstract val historyDAO : HistoryDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            Log.d("DB","getInstance called")
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room
                        .databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            DATABASE_NAME
                        )
                        .createFromAsset("database/$DATABASE_NAME")
                        .build()
                }
                Log.d("DB","getInstance returning instance $instance")
                return instance
            }
        }
    }
}
