package com.example.memoapp_room.memo.utils

import android.util.Log
import java.lang.Exception
import java.text.SimpleDateFormat

class DateFormatUtil {
    companion object {
        val keyDateFormat = SimpleDateFormat("yyyyMMdd")
        val titleDateFormat = SimpleDateFormat("yyyy년 MM월 dd일")

        fun setTitle(inputDate: String) : String {
            try {
                val date = keyDateFormat.parse(inputDate)
                return titleDateFormat.format(date)

            } catch (e: Exception) {
                Log.d("yj", "title parse exception : $e")
                return ""
            }
        }
    }
}