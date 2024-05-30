package com.example.memoapp_room.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.memoapp_room.Converters

// Local DB
@Database(entities = [MemoEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class MemoDataBase : RoomDatabase() {
    abstract fun memoDAO(): MemoDAO

    companion object {  // singleTon pattern
        private var INSTANCE: MemoDataBase? = null

        fun getInstance(context: Context): MemoDataBase? {
            if (INSTANCE == null) {
                synchronized(MemoDataBase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        MemoDataBase::class.java,
                        "memo.db"
                    ).fallbackToDestructiveMigration()
                        .build()  // 새로운 데이터로 덮어씀
                }
            }

            return INSTANCE
        }

    }
}
