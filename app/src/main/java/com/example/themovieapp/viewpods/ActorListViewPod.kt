package com.example.themovieapp.viewpods

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.themovieapp.adapters.ActorAdapter
import com.example.themovieapp.adapters.MovieAdapter
import kotlinx.android.synthetic.main.view_pod_actor_list.view.*

class ActorListViewPod @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RelativeLayout(context, attrs) {
    lateinit var mActorAdapter: ActorAdapter;

    override fun onFinishInflate() {
        setUpActorRecyclerView()
        super.onFinishInflate()
    }

    private fun setUpActorRecyclerView() {
        mActorAdapter = ActorAdapter()
        rvActors.adapter = mActorAdapter
        rvActors.layoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)
    }

}