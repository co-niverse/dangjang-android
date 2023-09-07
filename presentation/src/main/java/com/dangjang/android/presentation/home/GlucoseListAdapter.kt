package com.dangjang.android.presentation.home

import android.view.LayoutInflater
import android.view.View
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
            holder.setFeedbackContentVisibility()
        }
        holder.binding.glucoseEditBtn.setOnClickListener {
            mItemClickListener.onItemClick(glucoseList[position])
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
                binding.glucoseEditBtn.visibility = View.VISIBLE
                binding.glucoseListUpIv.setImageDrawable(itemView.context.getDrawable(com.dangjang.android.presentation.R.drawable.ic_arrow_up_gray))
            } else {
                binding.glucoseFeedbackTitleTv.visibility = View.GONE
                binding.glucoseFeedbackContentTv.visibility = View.GONE
                binding.glucoseEditBtn.visibility = View.GONE
                binding.glucoseListUpIv.setImageDrawable(itemView.context.getDrawable(com.dangjang.android.presentation.R.drawable.ic_arrow_down_green)) }
        }

        fun bind(glucoseList: GlucoseListVO) {
            binding.glucoseListTimeTv.text = glucoseList.time
            binding.glucoseNumberTv.text = glucoseList.glucose.toString()
            binding.glucoseFeedbackTitleTv.text = glucoseList.feedbackTitle
            binding.glucoseFeedbackContentTv.text = glucoseList.feedbackContent
        }
    }

}