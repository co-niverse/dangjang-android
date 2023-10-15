package com.dangjang.android.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dangjang.android.domain.model.AlarmListVO
import com.dangjang.android.presentation.databinding.ItemAlramListBinding

class AlarmListAdapter(
    private val viewModel: HomeViewModel
) : ListAdapter<AlarmListVO, RecyclerView.ViewHolder>(diffUtil) {

    interface MyItemClickListener {
        fun onItemClick(alarmListVO: AlarmListVO)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemAlramListBinding =
            ItemAlramListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(getItem(position))
    }

    inner class ViewHolder(val binding: ItemAlramListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(alarmList: AlarmListVO) {
            binding.vm = viewModel
            binding.alarmList = alarmList
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<AlarmListVO>() {
            override fun areContentsTheSame(oldItem: AlarmListVO, newItem: AlarmListVO) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: AlarmListVO, newItem: AlarmListVO) =
                oldItem.title == newItem.title
        }
    }

}