package com.dangjang.android.presentation.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dangjang.android.domain.model.GlucoseListVO
import com.dangjang.android.presentation.databinding.ItemGlucoseListBinding

class GlucoseListAdapter(
    private val viewModel: HomeViewModel
) : ListAdapter<GlucoseListVO, RecyclerView.ViewHolder>(diffUtil){

    interface MyItemClickListener {
        fun onEditBtnClick(glucoseList: GlucoseListVO)
        fun onDeleteBtnClick(glucoseList: GlucoseListVO)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemGlucoseListBinding =
            ItemGlucoseListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(getItem(position))
        holder.itemView.setOnClickListener {
            holder.setFeedbackContentVisibility()
        }
        holder.binding.glucoseEditBtn.setOnClickListener {
            mItemClickListener.onEditBtnClick(getItem(position))
        }
        holder.binding.glucoseDeleteBtn.setOnClickListener {
            mItemClickListener.onDeleteBtnClick(getItem(position))
        }
    }

    inner class ViewHolder(val binding: ItemGlucoseListBinding) :
    RecyclerView.ViewHolder(binding.root) {
        private var isExpanded = false

        fun setFeedbackContentVisibility() {
            isExpanded = !isExpanded
            if (isExpanded) {
                binding.glucoseFeedbackTitleTv.visibility = View.VISIBLE
                binding.glucoseFeedbackContentTv.visibility = View.VISIBLE
                binding.glucoseBtnLl.visibility = View.VISIBLE
                binding.glucoseListUpIv.setImageDrawable(itemView.context.getDrawable(com.dangjang.android.presentation.R.drawable.ic_arrow_up_gray))
            } else {
                binding.glucoseFeedbackTitleTv.visibility = View.GONE
                binding.glucoseFeedbackContentTv.visibility = View.GONE
                binding.glucoseBtnLl.visibility = View.GONE
                binding.glucoseListUpIv.setImageDrawable(itemView.context.getDrawable(com.dangjang.android.presentation.R.drawable.ic_arrow_down_green)) }
        }

        fun bind(glucoseList: GlucoseListVO) {
            binding.vm = viewModel
            binding.glucoseList = glucoseList
            //TODO : 태그 설정
            binding.glucoseListTagIv.setImageDrawable(itemView.context.getDrawable(glucoseList.alertIcon))
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<GlucoseListVO>() {
            override fun areContentsTheSame(oldItem: GlucoseListVO, newItem: GlucoseListVO) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: GlucoseListVO, newItem: GlucoseListVO) =
                oldItem.glucose == newItem.glucose
        }
    }

}