package com.example.memoapp_room

import android.app.Application
import com.example.memoapp_room.room.MemoDataBase
import com.example.memoapp_room.room.MemoRepository

class MemoApplication : Application() {

    private val memoDB by lazy { MemoDataBase.getInstance(this) }

    val memoRepository by lazy { MemoRepository(memoDB!!.memoDAO()) }
}