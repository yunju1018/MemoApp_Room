package com.example.memoapp_room.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.memoapp_room.room.MemoEntity
import com.example.memoapp_room.room.MemoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MemoViewModel(private val memoRepository: MemoRepository) : ViewModel() {

    val memoList: LiveData<List<MemoEntity>> = memoRepository.getMomoList()

    fun insertMemo(memo: MemoEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            memoRepository.insertMemo(memo)
        }
    }

    fun deleteMemo(memo: MemoEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            memoRepository.deleteMemo(memo)
        }
    }

    fun deleteAll() {
        CoroutineScope(Dispatchers.IO).launch {
            memoRepository.deleteAll()
        }
    }
}