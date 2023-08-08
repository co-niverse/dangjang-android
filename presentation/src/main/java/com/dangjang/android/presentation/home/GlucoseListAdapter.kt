package com.dangjang.android.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dangjang.android.domain.model.GlucoseListVO
import com.dangjang.android.presentation.databinding.ItemGlucoseListBinding

class GlucoseListAdapter(
    private val glucoseList: ArrayList<GlucoseListVO>
) : RecyclerView.Adapter<GlucoseListAdapter.ViewHolder>(){

    interface MyItemClickListener {
        fun onItemClick(glucoseList: GlucoseListVO)
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

    override fun getItemCount(): Int {
        return glucoseList.size
    }

    override fun onBindViewHolder(holder: GlucoseListAdapter.ViewHolder, position: Int) {
        holder.bind(glucoseList[position])
        holder.itemView.setOnClickListener {
            mItemClickListener.onItemClick(glucoseList[position])
        }
    }

    inner class ViewHolder(val binding: ItemGlucoseListBinding) :
    RecyclerView.ViewHolder(binding.root) {
        fun bind(glucoseList: GlucoseListVO) {
            binding.glucoseListTimeTv.text = glucoseList.time
            binding.glucoseFeedbackTitleTv.text = glucoseList.feedbackTitle
            binding.glucoseFeedbackContentTv.text = glucoseList.feedbackContent
        }
    }

}