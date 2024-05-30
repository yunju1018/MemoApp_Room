package com.example.memoapp_room

import androidx.room.TypeConverter
import com.example.memoapp_room.memo.models.MemoData
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun listToJson(value: List<MemoData>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): List<MemoData>? {
        return Gson().fromJson(value, Array<MemoData>::class.java)?.toList()
    }
}