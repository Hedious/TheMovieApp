package com.example.themovieapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.themovieapp.R
import com.example.themovieapp.viewholders.MovieViewHolder
import com.example.themovieapp.viewholders.ShowCaseViewHolder

class ShowCaseAdapter : RecyclerView.Adapter<ShowCaseViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowCaseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_showcase,parent,false)
        return ShowCaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShowCaseViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 10;
    }
}