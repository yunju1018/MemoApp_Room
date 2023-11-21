package com.example.memoapp_room.memo

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
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
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("StaticFieldLeak")
class MainActivity : AppCompatActivity() {
    private lateinit var memoAdapter: MemoAdapter
    private lateinit var binding: ActivityMainBinding

    private val date = Calendar.getInstance()
    private val keyDateFormat = SimpleDateFormat("yyMMdd")
    private val simpleDateFormat = SimpleDateFormat("yy년 MM월 dd일")

    private val viewModel by lazy {
        MemoViewModelFactory((application as MemoApplication).memoRepository).create(MemoViewModel::class.java)
    }

    private val datePickerListener = DatePickerDialog.OnDateSetListener { p0, year, month, day ->
        Log.d("yj", "year : $year, month : $month, day : $day")
        date.set(year, month, day)
        binding.title.text = simpleDateFormat.format(date.time)
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
            val key = keyDateFormat.format(date.time)
            val memo = MemoEntity(key.toLong(), binding.edtMemo.text.toString())
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

        binding.title.text = simpleDateFormat.format(date.time)
        binding.title.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        DatePickerDialog(
            this,
            datePickerListener,
            date.get(Calendar.YEAR),
            date.get(Calendar.MONTH),
            date.get((Calendar.DAY_OF_MONTH))
        ).show()
    }
}