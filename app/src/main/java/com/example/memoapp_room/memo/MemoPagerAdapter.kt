package com.example.memoapp_room.memo

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import com.example.memoapp_room.room.MemoEntity

class MemoPagerAdapter(
    val fa: FragmentActivity,
    val callback: (Long?, String) -> Unit
) : FragmentStateAdapter(fa) {

    private var memoList = listOf<MemoEntity>()
    private val TAG = MemoPagerAdapter::class.simpleName
    override fun getItemCount(): Int = if (memoList.isNotEmpty()) memoList.size else 0

    override fun createFragment(position: Int): Fragment {
        Log.d(TAG, "createFragment : ${memoList}")
        return MemoDataFragment.newInstance(memoList[position]) { key, Data ->
            callback.invoke(key, Data)
        }
    }

    fun setList(memoList: List<MemoEntity>) {
        Log.d(TAG, "setList : ${memoList}")
        this.memoList = memoList
    }

    override fun onBindViewHolder(
        holder: FragmentViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        val fragment  = fa.supportFragmentManager.findFragmentByTag("f$position")
        fragment?.let{
            if( it is MemoDataFragment){
                it.setData(memoList[position])
            }
        }
        super.onBindViewHolder(holder, position, payloads)
    }
}