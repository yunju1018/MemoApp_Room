package com.example.memoapp_room.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.memoapp_room.room.MemoRepository

// ViewModel 에서 생성자 받기 위해 VIewModelFactory 사용
class MemoViewModelFactory(private val repository: MemoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // 상속 / 구현 여부 확인
        if (modelClass.isAssignableFrom(MemoViewModel::class.java)) {
            return MemoViewModel(repository) as T
        }
        throw IllegalArgumentException("MemoViewModel is not")
    }
}