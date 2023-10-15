package com.dangjang.android.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dangjang.android.domain.model.ProductVO
import com.dangjang.android.presentation.databinding.ItemGiftListBinding
import com.dangjang.android.presentation.mypage.MypageViewModel

class GiftListAdapter(
    private val viewModel: MypageViewModel
) : ListAdapter<ProductVO, RecyclerView.ViewHolder>(diffUtil) {

    interface MyItemClickListener {
        fun onItemClick(giftListItem: ProductVO)
    }

    private lateinit var mItemClickListener: MyItemClickListener
    private var selectedPosition: Int = RecyclerView.NO_POSITION

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: ItemGiftListBinding =
            ItemGiftListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(getItem(position))

        if (position == selectedPosition) {
            holder.binding.giftListCl.setBackgroundResource(com.dangjang.android.presentation.R.drawable.background_border_green)
        } else {
            holder.binding.giftListCl.setBackgroundResource(com.dangjang.android.presentation.R.drawable.background_white_gradient)
        }

        holder.itemView.setOnClickListener {
            mItemClickListener.onItemClick(getItem(position))

            val previousSelectedPosition = selectedPosition
            selectedPosition = position

            notifyItemChanged(previousSelectedPosition)
            notifyItemChanged(selectedPosition)
        }
    }

    inner class ViewHolder(val binding: ItemGiftListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(giftListItem: ProductVO) {
            binding.vm = viewModel
            binding.giftList = giftListItem
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ProductVO>() {
            override fun areContentsTheSame(oldItem: ProductVO, newItem: ProductVO) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: ProductVO, newItem: ProductVO) =
                oldItem.title == newItem.title
        }
    }
}
