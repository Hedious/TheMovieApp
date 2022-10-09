package com.example.themovieapp.mvp.presenters

import androidx.lifecycle.LifecycleOwner
import com.example.themovieapp.mvp.views.BaseView
import com.example.themovieapp.mvp.views.MainView

interface IBasePresenter {
    fun onUiReady(owner: LifecycleOwner)
}