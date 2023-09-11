package com.dangjang.android.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dangjang.android.domain.model.GlucoseGuideVO
import com.dangjang.android.presentation.databinding.ItemGlucoseGuideListBinding

class GlucoseGuideAdapter(
    private val glucoseGuideList: ArrayList<GlucoseGuideVO>
) : RecyclerView.Adapter<GlucoseGuideAdapter.ViewHolder>() {

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

    override fun onBindViewHolder(holder: GlucoseGuideAdapter.ViewHolder, position: Int) {
        holder.bind(glucoseGuideList[position])
        holder.binding.glucoseGuideListCl.setOnClickListener {
            mItemClickListener.onItemClick(glucoseGuideList[position])
        }
    }

    override fun getItemCount(): Int {
        return glucoseGuideList.size
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
                    binding.glucoseGuideListTitleTv.text = glucoseGuideList.guideName
                    binding.glucoseGuideListCountTv.text = glucoseGuideList.guideCount
                    binding.glucoseGuideListCl.background = itemView.context.getDrawable(glucoseGuideList.guideBackground)
                }
            }
}