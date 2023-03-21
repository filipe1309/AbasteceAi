package com.filipe1309.abasteceai.libraries.database

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.OnConflictStrategy
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.filipe1309.abasteceai.libraries.database.model.Fuel


const val DATABASE_NAME = "abasteceai_database.db"

@Database(entities = [Fuel::class], version = 2)
abstract class AppDatabase: RoomDatabase() {
    abstract val fuelDAO : FuelDAO

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
                        .addMigrations(MIGRATION_1_2)
                        .fallbackToDestructiveMigration()
                        .build()
                }
                Log.d("DB","getInstance returning instance $instance")
                return instance
            }
        }
        private val MIGRATION_1_2 = object: Migration(1,2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                Log.d("MIG1-2", "Migration is running")
                val cv = ContentValues()
                cv.put("name", "Gasolina")
                cv.put("price", 5.00)
                cv.put("efficiency", 10.0)
                cv.put("efficiency_unit", "km/l")
                database.insert("fuels", OnConflictStrategy.IGNORE, cv)
                cv.clear()
                cv.put("name", "Etanol")
                cv.put("price", 3.50)
                cv.put("efficiency", 7.0)
                cv.put("efficiency_unit", "km/l")
                database.insert("fuels", OnConflictStrategy.IGNORE, cv)
                cv.clear()
                cv.put("name", "Diesel")
                cv.put("price", 4.00)
                cv.put("efficiency", 8.0)
                cv.put("efficiency_unit", "km/l")
                database.insert("fuels", OnConflictStrategy.IGNORE, cv)
            }
        }
    }
}
