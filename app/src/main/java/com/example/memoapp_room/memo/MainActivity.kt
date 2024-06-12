package com.example.memoapp_room.memo

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.example.memoapp_room.MemoApplication
import com.example.memoapp_room.R
import com.example.memoapp_room.databinding.ActivityMainBinding
import com.example.memoapp_room.memo.models.MemoData
import com.example.memoapp_room.memo.utils.DateFormatUtil
import com.example.memoapp_room.room.MemoEntity
import com.example.memoapp_room.viewmodel.MemoViewModel
import com.example.memoapp_room.viewmodel.MemoViewModelFactory
import java.util.*

@SuppressLint("StaticFieldLeak")
class MainActivity : AppCompatActivity() {
    private lateinit var memoPagerAdapter: MemoPagerAdapter
    private lateinit var binding: ActivityMainBinding

    private val date = Calendar.getInstance()
    private var key : String = ""
    private val TAG = MainActivity::class.simpleName

    private val viewModel by lazy {
        MemoViewModelFactory((application as MemoApplication).memoRepository).create(MemoViewModel::class.java)
    }

    private val datePickerListener = DatePickerDialog.OnDateSetListener { p0, year, month, day ->
        Log.d("yj", "year : $year, month : $month, day : $day")
        val selectDate = Calendar.getInstance()
        selectDate.set(year, month, day)

        if (selectDate != date) {
            val newMemo = MemoEntity(DateFormatUtil.keyDateFormat.format(selectDate.time), mutableListOf())
            viewModel.insertMemo(newMemo)
        }
        date.set(year, month, day)
        binding.title.text = DateFormatUtil.titleDateFormat.format(date.time)
        setToday()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setToday()
        setLayout()
        observe()
    }

    private fun observe() {
        viewModel.memoList.observe(this, Observer {
            memoPagerAdapter.setList(it)

            it.forEachIndexed { index, memoEntity ->
                if (key.isNotEmpty() && memoEntity.id == key) {
                    binding.viewPager.currentItem = index
                }
            }
        })
    }

    private fun setLayout() {
        val memoList = viewModel.memoList
        Log.d(TAG, "setLayout memoList : ${memoList.value?.size}")

        memoPagerAdapter = MemoPagerAdapter(this) {key, data ->

            viewModel.getMemoData(key) { memoEntity ->
                var memoData : MutableList<MemoData> = mutableListOf()
                memoEntity?.memoList?.let { memoDataList ->
                    if (memoDataList.contains(MemoData(data))) {
                        memoData = memoDataList.filterNot { it == MemoData(data) }.toMutableList()
                    }
                }
                val memo = MemoEntity(key, memoData)
                viewModel.insertMemo(memo)
            }

        }
        binding.viewPager.apply {
            adapter = memoPagerAdapter
            offscreenPageLimit = 3
        }

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                Log.d(TAG, "registerOnPageChangeCallback position: $position")
                Log.d(TAG, "memoList.value.size: ${memoList.value?.size}")

                if (memoList.value?.isNotEmpty() == true) {
                    key = memoList.value?.get(position)?.id.toString()
                    binding.title.text = DateFormatUtil.setTitle(key)
                } else {
                    setToday()
                }
            }
        })

        binding.addButton.setOnClickListener {
            Log.d(TAG, "key : $key")
            if(key.isEmpty()) {
                setToday()
            }

            viewModel.getMemoData(key) {memoEntity ->
                val memoData : MutableList<MemoData> = mutableListOf()
                memoEntity?.memoList?.let { it ->
                    memoData.addAll(it)
                }

                memoData.add(MemoData(binding.edtMemo.text.toString()))
                val memo = MemoEntity(key, memoData)
                viewModel.insertMemo(memo)
                binding.edtMemo.setText("")
            }
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

        binding.title.text = DateFormatUtil.setTitle(key)
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

    private fun setToday() {
        key = DateFormatUtil.keyDateFormat.format(date.time)
    }
}