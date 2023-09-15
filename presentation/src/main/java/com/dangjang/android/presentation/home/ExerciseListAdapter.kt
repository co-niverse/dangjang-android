package com.dangjang.android.presentation.home

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dangjang.android.domain.model.ExerciseListVO
import com.dangjang.android.presentation.databinding.ItemExerciseListBinding

class ExerciseListAdapter(
    private val viewModel: HomeViewModel
) : ListAdapter<ExerciseListVO, RecyclerView.ViewHolder>(diffUtil) {

    interface MyItemClickListener {
        fun onItemClick(exerciseList: ExerciseListVO)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: ItemExerciseListBinding =
            ItemExerciseListBinding.inflate(
                android.view.LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(getItem(position))
        holder.itemView.setOnClickListener {
            mItemClickListener.onItemClick(getItem(position))
        }
    }

    inner class ViewHolder(val binding: ItemExerciseListBinding) :
            RecyclerView.ViewHolder(binding.root) {
                fun bind(exerciseList: ExerciseListVO) {
                    binding.vm = viewModel
                    binding.exerciseList = exerciseList
                }
            }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ExerciseListVO>() {
            override fun areContentsTheSame(oldItem: ExerciseListVO, newItem: ExerciseListVO) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: ExerciseListVO, newItem: ExerciseListVO) =
                oldItem.exerciseName == newItem.exerciseName
        }
    }
}