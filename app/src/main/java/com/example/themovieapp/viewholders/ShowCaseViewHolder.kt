package com.example.themovieapp.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.themovieapp.delegate.ShowcaseViewHolderDelegate

class ShowCaseViewHolder(itemView: View,private  val mDelegate: ShowcaseViewHolderDelegate) : RecyclerView.ViewHolder(itemView) {

    init{
       itemView.setOnClickListener {
           mDelegate.onTapMovieFromShowcase()
       }
    }
}