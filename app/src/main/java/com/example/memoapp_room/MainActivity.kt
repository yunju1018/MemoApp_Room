package com.example.memoapp_room

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.memoapp_room.databinding.ActivityMainBinding
import kotlinx.coroutines.*

@SuppressLint("StaticFieldLeak")
class MainActivity : AppCompatActivity(), OnDeleteListener {
    lateinit var myAdapter: MyAdapter
    lateinit var binding: ActivityMainBinding
    lateinit var db: MemoDataBase
    var memoList = listOf<MemoEntity>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        MemoDataBase.getInstance(this)?.let {
            db = it
        }

        setLayout()
    }

    private fun setLayout() {
        myAdapter = MyAdapter(this, this)

        binding.recyclerView.adapter = myAdapter

        binding.button.setOnClickListener {
            val memo = MemoEntity(null, binding.edtMemo.text.toString())
            runBlocking {
                insertMemo(memo)
            }
            binding.edtMemo.setText("")
        }

        binding.deleteAll.setOnClickListener {
            runBlocking {
                deleteAll()
            }
        }

        runBlocking {
            getAllMemos()
        }
    }

    private suspend fun insertMemo(memo: MemoEntity) {
        // Background Thread 사용 (CoroutineScope)
        CoroutineScope(Dispatchers.IO).launch {
            db.memoDAO().insertMemo(memo)
            Log.d("yj", "CoroutineScope insertMemo")
        }.join()
        getAllMemos()
    }

    private suspend fun getAllMemos() {
        CoroutineScope(Dispatchers.IO).launch {
            memoList = db.memoDAO().getAll()
            Log.d("yj", "CoroutineScope getAllMemos")
        }.join()
        changeMemoList(memoList)
    }

    private suspend fun deleteMemo(memo: MemoEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("yj", "CoroutineScope deleteMemo")
            db.memoDAO().delete(memo)
        }.join()
        getAllMemos()
    }

    private suspend fun deleteAll() {
        CoroutineScope(Dispatchers.IO).launch {
            db.memoDAO().deleteAll()
            Log.d("yj", "CoroutineScope deleteAll")
        }.join()
        getAllMemos()
    }

    private fun changeMemoList(memoList: List<MemoEntity>) {
        myAdapter.setList(memoList)
        Log.d("yj", "setRecyclerView().Called")
    }

    override fun onDeleteListener(memo: MemoEntity) {
        Log.d("yj", "onDeleteListener.Called")
        runBlocking {
            deleteMemo(memo)
        }
    }
}