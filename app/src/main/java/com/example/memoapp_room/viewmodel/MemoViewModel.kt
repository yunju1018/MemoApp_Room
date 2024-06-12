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
    // Repository Pattern
    // View -> ViewModel 데이터 가져옴
    // ViewModel -> Repository 로 데이터 접근
    // Repository -> DataSource(local/remote) 로부터 데이터 요청

    val memoList: LiveData<List<MemoEntity>> = memoRepository.getMomoList()
    fun getMemoData(id: String, onResult:(MemoEntity?) -> Unit) {
        viewModelScope.launch {
            val memo = memoRepository.getMemoData(id)
            onResult(memo)
        }
    }

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