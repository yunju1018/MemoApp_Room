package com.example.memoapp_room

import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.REPLACE

// Data 접근(Access) 쿼리 정의
@Dao
interface MemoDAO {
    @Insert(onConflict = REPLACE)   // primary key 같을 경우 덮어쓰기
    fun insertMemo(memo : MemoEntity)

    @Query("SELECT * FROM memo")
    fun getAll() : List<MemoEntity>

    @Delete
    fun delete(memo : MemoEntity)

    @Query("DELETE FROM memo")
    fun deleteAll()
}