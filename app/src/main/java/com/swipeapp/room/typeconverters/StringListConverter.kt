package com.swipeapp.room.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.swipeapp.network.ApiService
import java.lang.reflect.Type

/**
 * This is used to explain Room DB how to store and get List<String>
 */
class StringListConverter {

    @TypeConverter
    fun fromStringList(strings: List<String?>?): String? {
        if (strings == null) {
            return null
        }
        val gson = Gson()
        val type: Type =
            object : TypeToken<List<String?>?>() {}.type
        return gson.toJson(strings, type)
    }

    @TypeConverter
    fun toStringList(stringsString: String?): List<String>? {
        if (stringsString == null) {
            return null
        }
        val gson = Gson()
        val type: Type =
            object : TypeToken<List<String?>?>() {}.type
        return gson.fromJson<List<String>>(stringsString, type)
    }
    
}