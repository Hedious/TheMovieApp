package com.example.themovieapp.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.themovieapp.delegate.BannerViewHolderDelegate

class BannerViewHolder(itemView: View,private val mDelegate: BannerViewHolderDelegate) : RecyclerView.ViewHolder(itemView) {

    init{
        itemView.setOnClickListener {
            mDelegate.onTapMovieFromBanner()
        }
    }
}