package com.example.memoapp_room.memo

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.memoapp_room.MemoApplication
import com.example.memoapp_room.R
import com.example.memoapp_room.databinding.ActivityMainBinding
import com.example.memoapp_room.room.MemoEntity
import com.example.memoapp_room.viewmodel.MemoViewModel
import com.example.memoapp_room.viewmodel.MemoViewModelFactory
import kotlinx.coroutines.*

@SuppressLint("StaticFieldLeak")
class MainActivity : AppCompatActivity() {
    lateinit var memoAdapter: MemoAdapter
    lateinit var binding: ActivityMainBinding

    private val viewModel by lazy {
        MemoViewModelFactory((application as MemoApplication).memoRepository).create(MemoViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setLayout()
        observe()
    }

    private fun observe() {
        viewModel.memoList.observe(this, Observer {
            memoAdapter.setList(it)
        })
    }

    private fun setLayout() {
        memoAdapter = MemoAdapter(this) {
            viewModel.deleteMemo(it)
        }

        binding.recyclerView.adapter = memoAdapter

        binding.button.setOnClickListener {
            val memo = MemoEntity(null, binding.edtMemo.text.toString())
            viewModel.insertMemo(memo)
            binding.edtMemo.setText("")
        }

        binding.deleteAll.setOnClickListener {
                AlertDialog.Builder(this)
                    .setTitle("전체 삭제하시겠습니까?")
                    .setPositiveButton(
                        "예"
                    ) { p0, p1 -> viewModel.deleteAll() }
                    .setNegativeButton("아니오", null)
                    .create()
                    .show()

        }
    }
}