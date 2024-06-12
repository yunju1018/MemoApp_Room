package com.example.memoapp_room.room

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.memoapp_room.room.MemoDAO
import com.example.memoapp_room.room.MemoEntity

class MemoRepository(private val memoDao: MemoDAO) {

    // insert, delete 동시 호출 시 db 작업에 문제가 생길 수 있어서 suspend 를 붙여서 비동기 처리를 일시 정지 후 작업
    @WorkerThread
    fun insertMemo(memo: MemoEntity) {
        memoDao.insertMemo(memo)
    }

    @WorkerThread
    fun deleteMemo(memo: MemoEntity) {
        memoDao.delete(memo)
    }

    fun getMomoList(): LiveData<List<MemoEntity>> {
        return memoDao.getAll()
    }

    fun getMemoData(memoId: String): LiveData<MemoEntity> {
        return memoDao.getMemo(memoId)
    }

    @WorkerThread
    fun deleteAll() {
        memoDao.deleteAll()
    }
}