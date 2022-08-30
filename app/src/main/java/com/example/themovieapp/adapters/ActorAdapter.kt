package com.example.themovieapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.themovieapp.R
import com.example.themovieapp.data.vos.ActorVO
import com.example.themovieapp.data.vos.MovieVO
import com.example.themovieapp.viewholders.ActorViewHolder

class ActorAdapter : RecyclerView.Adapter<ActorViewHolder>() {

    private  var mAcotrs: List<ActorVO> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder_best_actor, parent, false)

        return ActorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        if(mAcotrs.isNotEmpty()){
            holder.bindData(mAcotrs[position])
        }
    }

    override fun getItemCount(): Int {
        return mAcotrs.count()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setNewData(actors: List<ActorVO>) {
        mAcotrs = actors
        notifyDataSetChanged()
    }
}