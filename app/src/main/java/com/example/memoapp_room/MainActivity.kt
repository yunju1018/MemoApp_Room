package com.example.memoapp_room

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.memoapp_room.databinding.ActivityMainBinding

@SuppressLint("StaticFieldLeak")
class MainActivity : AppCompatActivity(), OnDeleteListener {

    lateinit var binding: ActivityMainBinding
    lateinit var db: MemoDataBase
    var memoList = listOf<MemoEntity>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        db = MemoDataBase.getInstance(this)!!

        binding.button.setOnClickListener {
            val memo = MemoEntity(null, binding.edtMemo.text.toString())
            insertMemo(memo)
            binding.edtMemo.setText("")
        }

        getAllMemos()
    }

    fun insertMemo(memo: MemoEntity) {
        // Background Thread 사용
        val insertTask = object : AsyncTask<Unit, Unit, Unit>() {
            override fun doInBackground(vararg p0: Unit?) {
                db.memoDAO().insertMemo(memo)
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                getAllMemos()
            }
        }
        insertTask.execute()
    }

    fun getAllMemos() {
        val getAllTask = object : AsyncTask<Unit, Unit, Unit>() {
            override fun doInBackground(vararg p0: Unit?) {
                memoList = db.memoDAO().getAll()
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                setRecyclerView(memoList)
            }
        }
        getAllTask.execute()
    }

    fun deleteMemo(memo: MemoEntity) {
        val deleteTask = object : AsyncTask<Unit, Unit, Unit>() {
            override fun doInBackground(vararg p0: Unit?) {
                db.memoDAO().delete(memo)
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                getAllMemos()
            }
        }
        deleteTask.execute()
    }

    fun setRecyclerView(memoList: List<MemoEntity>) {
        binding.recyclerView.adapter = MyAdapter(this, memoList, this)
    }

    override fun onDeleteListener(memo: MemoEntity) {
        deleteMemo(memo)
    }
}