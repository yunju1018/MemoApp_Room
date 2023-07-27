package com.example.memoapp_room

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.memoapp_room.databinding.ItemMemoBinding

class MyAdapter(val context: Context, val list: List<MemoEntity>, val listener: OnDeleteListener) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemMemoBinding =
            DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_memo, parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val memo: MemoEntity = list[position]
        holder.bind(memo)
    }

    inner class MyViewHolder(val binding: ItemMemoBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(memo: MemoEntity) {
            binding.textViewMemo.text = memo.memo

            itemView.setOnLongClickListener {
                listener.onDeleteListener(memo)
                return@setOnLongClickListener true
            }
        }
    }
}