package com.example.themovieapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.themovieapp.R
import com.example.themovieapp.adapters.BannerAdapter
import com.example.themovieapp.adapters.ShowCaseAdapter
import com.example.themovieapp.data.models.MovieModel
import com.example.themovieapp.data.models.MovieModelImpl
import com.example.themovieapp.data.vos.GenreVO
import com.example.themovieapp.data.vos.MovieVO
import com.example.themovieapp.delegate.BannerViewHolderDelegate
import com.example.themovieapp.delegate.MovieViewHolderDelegate
import com.example.themovieapp.delegate.ShowcaseViewHolderDelegate
import com.example.themovieapp.dummy.dummyGenreList
import com.example.themovieapp.mvi.intents.MainIntent
import com.example.themovieapp.mvi.mvibase.MVIView
import com.example.themovieapp.mvi.states.MainState
import com.example.themovieapp.mvi.viewmodels.MainViewModel
import com.example.themovieapp.network.dataagents.MovieDataAgentImpl
import com.example.themovieapp.network.dataagents.OkHttpDataAgentImpl
import com.example.themovieapp.viewpods.ActorListViewPod
import com.example.themovieapp.viewpods.MovieListViewPod
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity() : AppCompatActivity(), BannerViewHolderDelegate, ShowcaseViewHolderDelegate,
    MovieViewHolderDelegate, MVIView<MainState> {

    lateinit var mBannerAdapter: BannerAdapter
    lateinit var mShowCaseAdapter: ShowCaseAdapter

    lateinit var mBestPopularMovieListViewPod: MovieListViewPod
    lateinit var mMovieByGenreViewPod: MovieListViewPod
    lateinit var mActorListViewPod: ActorListViewPod

    //ViewModel
    private lateinit var mViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //setup ViewModel
        setUpViewModel()

        setUpToolBar()
        setUpViewPod()
        setUpBannerViewPager()
        setUpShowCaseViewPager()
        setUpListeners()

        //Set Initial Intents
        setInitialIntents()
        observeState()
    }

    private fun observeState() {
        mViewModel.state.observe(this, this::render)
    }

    private fun setUpViewModel() {
        mViewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }

    private fun setInitialIntents() {
        mViewModel.processIntent(MainIntent.LoadAllHomePageData, this)
    }

    private fun showError(message: String) {
        Snackbar.make(
            window.decorView,
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun setUpViewPod() {
        mBestPopularMovieListViewPod = vpBestPopularMovieList as MovieListViewPod
        mMovieByGenreViewPod = vpMoviesByGenre as MovieListViewPod

        mBestPopularMovieListViewPod.setUpMovieListViewPod(this)
        mMovieByGenreViewPod.setUpMovieListViewPod(this)

        mActorListViewPod = vpActorList as ActorListViewPod
    }

    private fun setUpListeners() {
        //Genre Tab Layout
        tabLayoutGenre.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                mViewModel.processIntent(
                    MainIntent.LoadMoviesByGenreIntent(tab?.position ?: 0),
                    this@MainActivity
                )
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })


    }


    private fun setUpBannerViewPager() {
        mBannerAdapter = BannerAdapter(this)
        viewPagerBanner.adapter = mBannerAdapter

        dotsIndicatorBanner.attachTo(viewPagerBanner)

    }

    private fun setUpToolBar() {
        //App Bar Leading Icon
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_discover, menu)
        return true;
    }

    private fun setUpGenreTabLayout(genreList: List<GenreVO>) {
        genreList.forEach {
            val tab = tabLayoutGenre.newTab()
            tab.text = it.name
            tabLayoutGenre.addTab(tab)
        }
    }

    private fun setUpShowCaseViewPager() {
        mShowCaseAdapter = ShowCaseAdapter(this);
        rvShowcases.adapter = mShowCaseAdapter
        rvShowcases.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    override fun onTapMovieFromBanner(movieId: Int) {
        //Snackbar.make(window.decorView, "Tapped Movie From Banner", Snackbar.LENGTH_LONG).show()
        startActivity(MovieDetailsActivity.newIntent(this, movieId = movieId))
    }

    override fun onTapMovieFromShowcase(movieId: Int) {
        //Snackbar.make(window.decorView, "Tapped Movie From Showcase", Snackbar.LENGTH_LONG).show()
        startActivity(MovieDetailsActivity.newIntent(this, movieId = movieId))
    }

    override fun onTapMovie(movieId: Int) {
//        Snackbar.make(
//            window.decorView,
//            "Tapped Movie From Best Popular Movies or Movies By Genre",
//            Snackbar.LENGTH_LONG
//        ).show()
        startActivity(MovieDetailsActivity.newIntent(this, movieId = movieId))
    }

    override fun render(state: MainState) {
        if (state.errorMessage.isNotEmpty()) {
            showError(state.errorMessage)
        }

        mBannerAdapter.setNewData(state.nowPlayingMovies)
        mBestPopularMovieListViewPod.setData(state.popularMovies)
        mShowCaseAdapter.setNewData(state.topRatedMovies)
        setUpGenreTabLayout(state.genres)
        mMovieByGenreViewPod.setData(state.moviesByGenre)
        mActorListViewPod.setData(state.actors)
    }


}