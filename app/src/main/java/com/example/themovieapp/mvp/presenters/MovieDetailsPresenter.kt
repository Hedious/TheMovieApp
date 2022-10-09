package com.example.themovieapp.mvp.presenters

import androidx.lifecycle.LifecycleOwner
import com.example.themovieapp.mvp.views.MainView
import com.example.themovieapp.mvp.views.MovieDetailsView

interface MovieDetailsPresenter : IBasePresenter {
    fun initView(view: MovieDetailsView)
    fun onUiReadyInMovieDetails(owner : LifecycleOwner, movieId : Int)
    fun onTapBack()
}