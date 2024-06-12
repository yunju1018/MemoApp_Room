package com.example.memoapp_room.room

import androidx.lifecycle.LiveData
import androidx.room.*

// Data 접근(Access) 쿼리 정의 interface
@Dao
interface MemoDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)   // primary key 같을 경우 덮어쓰기
    fun insertMemo(memo : MemoEntity)

    @Query("SELECT * FROM memo ORDER BY id ASC")
    fun getAll() : LiveData<List<MemoEntity>>

    @Query("SELECT * FROM memo WHERE id = :memoId")
    fun getMemo(memoId: String) : LiveData<MemoEntity>

    @Delete
    fun delete(memo : MemoEntity)

    @Query("DELETE FROM memo")
    fun deleteAll()
}