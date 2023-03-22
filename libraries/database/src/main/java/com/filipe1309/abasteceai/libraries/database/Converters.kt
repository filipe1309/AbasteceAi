package com.filipe1309.abasteceai.libraries.database

import android.location.Location
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.JsonParseException
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toLocation(locationString: String?): Location? {
        return try {
            Gson().fromJson(locationString, Location::class.java)
        } catch (e: JsonParseException) {
            null
        }
    }

    @TypeConverter
    fun toLocationString(location: Location?): String? {
        return Gson().toJson(location)
    }
}
