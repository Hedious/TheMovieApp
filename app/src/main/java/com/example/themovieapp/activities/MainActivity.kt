package com.example.themovieapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
import com.example.themovieapp.mvvm.MainViewModel
import com.example.themovieapp.network.dataagents.MovieDataAgentImpl
import com.example.themovieapp.network.dataagents.OkHttpDataAgentImpl
import com.example.themovieapp.viewpods.ActorListViewPod
import com.example.themovieapp.viewpods.MovieListViewPod
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity() : AppCompatActivity(), BannerViewHolderDelegate, ShowcaseViewHolderDelegate,
    MovieViewHolderDelegate {

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
        setUpViewModel()

        setUpToolBar()
        setUpViewPod()
        setUpBannerViewPager()
        setUpShowCaseViewPager()
        setUpListeners()

        //Observe Live Data
        observeLiveData()

    }



    private fun setUpViewModel() {
        mViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        mViewModel.getInitialData()
    }

    private fun observeLiveData() {
        mViewModel.nowPlayingMovieLiveData?.observe(this, mBannerAdapter::setNewData)
        mViewModel.popularMovieLiveData?.observe(this, mBestPopularMovieListViewPod::setData)
        mViewModel.topRatedMovieLiveData?.observe(this, mShowCaseAdapter::setNewData)
        mViewModel.genresLiveData.observe(this, this::setUpGenreTabLayout)
        mViewModel.moviesByGenreLiveData.observe(this, mMovieByGenreViewPod::setData)
        mViewModel.actorsLiveData.observe(this, mActorListViewPod::setData)

        mViewModel.mErrorLiveData.observe(this) {
            showError(it)
        }

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
                mViewModel.getMovieByGenre(tab?.position ?: 0)
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

        startActivity(MovieDetailsActivity.newIntent(this, movieId = movieId))
    }

    override fun onTapMovieFromShowcase(movieId: Int) {

        startActivity(MovieDetailsActivity.newIntent(this, movieId = movieId))
    }

    override fun onTapMovie(movieId: Int) {
        startActivity(MovieDetailsActivity.newIntent(this, movieId = movieId))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menuSearch -> {
                startActivity(MovieSearchActivity.newIntent(this))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}