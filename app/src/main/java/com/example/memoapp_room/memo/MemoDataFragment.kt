package com.example.memoapp_room.memo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.memoapp_room.R
import com.example.memoapp_room.databinding.FragmentMemoDataBinding
import com.example.memoapp_room.memo.models.MemoData
import com.example.memoapp_room.room.MemoEntity
import java.util.Arrays

class MemoDataFragment() : Fragment() {

    companion object {
        val TAG = MemoPagerAdapter::class.simpleName
        var callBack:((String, Int) -> Unit)? = null

        fun newInstance(data: MemoEntity, callback: ((String, Int) -> Unit)): MemoDataFragment {
            val args = Bundle()
            this.callBack = callback
            return MemoDataFragment().apply {
                arguments = args.apply {
                    putSerializable("data", data)
                }
            }
        }
    }

    private lateinit var adapter : MemoAdapter
    private lateinit var binding : FragmentMemoDataBinding
    private lateinit var data: MemoEntity
    private var key = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_memo_data, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        data = arguments?.getSerializable("data") as MemoEntity
        key = data.id
        Log.d(TAG, "fragment onViewCreated")
//        data = arguments?.getSerializable("data", MemoEntity::class.java)     yj : requires API level 33 (current min is 24)

        setLayout()
    }

    private fun setLayout() {
        adapter = MemoAdapter(requireContext()) {
            Log.d(TAG, "delete key : $key data : $it")
            callBack?.invoke(key, it)
        }
        setData(data)

        with(binding) {
            recyclerView.adapter = adapter
        }
        binding.recyclerView.adapter = adapter
    }

    fun setData(data: MemoEntity) {
        this.data = data
        adapter.setList(data.memoList)
    }
}