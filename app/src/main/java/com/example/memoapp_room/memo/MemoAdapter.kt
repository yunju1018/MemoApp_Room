package com.example.memoapp_room.memo

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.memoapp_room.R
import com.example.memoapp_room.databinding.ItemMemoBinding
import com.example.memoapp_room.room.MemoEntity

class MemoAdapter(val context: Context, private val callback: ((MemoEntity) -> Unit)) :
    RecyclerView.Adapter<MemoAdapter.MyViewHolder>() {
    private var memoList = listOf<MemoEntity>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemMemoBinding =
            DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_memo, parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return memoList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val memo: MemoEntity = memoList[position]
        holder.bind(memo)
    }

    fun setList(memoList: List<MemoEntity>) {
        this.memoList = memoList
        notifyDataSetChanged()
    }

    inner class MyViewHolder(private val binding: ItemMemoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(memo: MemoEntity) {
            binding.textViewMemo.text = memo.memo

            itemView.setOnLongClickListener(View.OnLongClickListener {
                AlertDialog.Builder(context)
                    .setTitle("삭제하시겠습니까?")
                    .setPositiveButton(
                        "예"
                    ) { p0, p1 -> callback.invoke(memo) }
                    .setNegativeButton("아니오", null)
                    .create()
                    .show()
                return@OnLongClickListener true
            })
        }
    }
}