package com.example.themovieapp.mvp.presenters

import com.example.themovieapp.delegate.BannerViewHolderDelegate
import com.example.themovieapp.delegate.MovieViewHolderDelegate
import com.example.themovieapp.delegate.ShowcaseViewHolderDelegate
import com.example.themovieapp.mvp.views.MainView
import com.example.themovieapp.viewholders.BannerViewHolder
import com.example.themovieapp.viewholders.ShowCaseViewHolder

interface MainPresenter : IBasePresenter, BannerViewHolderDelegate, ShowcaseViewHolderDelegate,
    MovieViewHolderDelegate {
    fun initView(view: MainView)
    fun onTapGenre(genrePosition: Int)
}