package com.example.memoapp_room.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Local DB
@Database(entities = arrayOf(MemoEntity::class), version = 1)
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
