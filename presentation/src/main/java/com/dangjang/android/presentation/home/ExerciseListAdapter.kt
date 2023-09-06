package com.dangjang.android.presentation.home

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dangjang.android.domain.model.ExerciseListVO
import com.dangjang.android.presentation.databinding.ItemExerciseListBinding

class ExerciseListAdapter(
    private val exerciseList: ArrayList<ExerciseListVO>
) : RecyclerView.Adapter<ExerciseListAdapter.ViewHolder>() {

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

    override fun getItemCount(): Int {
        return exerciseList.size
    }

    override fun onBindViewHolder(holder: ExerciseListAdapter.ViewHolder, position: Int) {
        holder.bind(exerciseList[position])
        holder.itemView.setOnClickListener {
            mItemClickListener.onItemClick(exerciseList[position])
        }
    }

    inner class ViewHolder(val binding: ItemExerciseListBinding) :
            RecyclerView.ViewHolder(binding.root) {
                fun bind(exerciseList: ExerciseListVO) {
                    binding.exerciseListTitleTv.text = exerciseList.exerciseName
                    binding.exerciseListHourTv.text = exerciseList.exerciseHour
                    binding.exerciseListMinuteTv.text = exerciseList.exerciseMinute
                }
            }
}