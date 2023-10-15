package com.dangjang.android.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dangjang.android.domain.model.GetGlucoseVO
import com.dangjang.android.domain.model.GlucoseGuideVO
import com.dangjang.android.presentation.databinding.ItemGlucoseGuideListBinding

class GlucoseGuideAdapter(
    private val viewModel: HomeViewModel
    //private val glucoseGuideList: ArrayList<GlucoseGuideVO>
) : ListAdapter<GlucoseGuideVO, RecyclerView.ViewHolder>(diffUtil) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: ItemGlucoseGuideListBinding = ItemGlucoseGuideListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(getItem(position))
        holder.binding.glucoseGuideListCl.setOnClickListener {
            mItemClickListener.onItemClick(getItem(position))
        }
    }

    interface MyItemClickListener {
        fun onItemClick(glucoseGuideList: GlucoseGuideVO)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: GlucoseGuideAdapter.MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    inner class ViewHolder(val binding: ItemGlucoseGuideListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(glucoseGuideList: GlucoseGuideVO) {
            binding.vm = viewModel
            binding.glucoseGuideList = glucoseGuideList
            binding.glucoseGuideListCl.background =
                itemView.context.getDrawable(glucoseGuideList.guideBackground)
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<GlucoseGuideVO>() {
            override fun areContentsTheSame(oldItem: GlucoseGuideVO, newItem: GlucoseGuideVO) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: GlucoseGuideVO, newItem: GlucoseGuideVO) =
                oldItem.guideName == newItem.guideName
        }
    }
}