package com.example.hanadroid.db

import androidx.room.TypeConverter
import com.example.hanadroid.model.Location
import com.google.gson.Gson

class LocationConverter {
    @TypeConverter
    fun fromString(locationString: String): Location {
        return Gson().fromJson(locationString, Location::class.java)
    }

    @TypeConverter
    fun toString(location: Location): String {
        return Gson().toJson(location)
    }
}
