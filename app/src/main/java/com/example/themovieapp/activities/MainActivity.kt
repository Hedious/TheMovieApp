package com.example.themovieapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
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
import com.example.themovieapp.network.dataagents.MovieDataAgentImpl
import com.example.themovieapp.network.dataagents.OkHttpDataAgentImpl
import com.example.themovieapp.network.dataagents.RetrofitDataAgentImpl
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

    //Model
    private val mMovieModel: MovieModel = MovieModelImpl

    //Data
    private var mGenres: List<GenreVO>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpToolBar()
        setUpViewPod()
        setUpBannerViewPager()
        //setUpGenreTabLayout()
        setUpShowCaseViewPager()
        setUpListeners()

        requestData()

//        mMovieModel.getNowPlayingMovies(
//            onSuccess = {
//                mBannerAdapter.setNewData(it)
//
//            },
//            onFailure = {
//                Snackbar.make(
//                    window.decorView,
//                    "Fail",
//                    Snackbar.LENGTH_LONG
//                ).show()
//            }
//        )


//        MovieDataAgentImpl.getNowPlayingMovies()
//        OkHttpDataAgentImpl.getNowPlayingMovies()
//        RetrofitDataAgentImpl.getNowPlayingMovies()
    }

    private fun requestData() {
        mMovieModel.getNowPlayingMovies(
            onSuccess = {
                mBannerAdapter.setNewData(it)

            },
            onFailure = {
                showError(it)
            }
        )

        mMovieModel.getPopularMovies(
            onSuccess = {
                mBestPopularMovieListViewPod.setData(it)
            },
            onFailure = {
                showError(it)
            }
        )

        mMovieModel.getTopRatedMovies(
            onSuccess = {
                mShowCaseAdapter.setNewData(it)
            },
            onFailure = {
                showError(it)
            }
        )

        mMovieModel.getGenres(
            onSuccess = {
                mGenres = it
                setUpGenreTabLayout(it)


                it.firstOrNull()?.id?.let { genreId ->
                    getMoviesByGenre(genreId)
                }
            },
            onFailure = {
                showError(it)
            }
        )

        mMovieModel.getActors(
            onSuccess = {
                mActorListViewPod.setData(it)
            },
            onFailure = {
                showError(it)
            }
        )
    }

    private fun showError(message: String) {
        Snackbar.make(
            window.decorView,
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun getMoviesByGenre(genreId: Int) {
        mMovieModel.getMoviesByGenre(genreId = genreId.toString(),
            onSuccess = {
                mMovieByGenreViewPod.setData(it)
            },
            onFailure = {
                showError(it)
            }
        )
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
                mGenres?.get(tab?.position ?: 0)?.id?.let {
                    getMoviesByGenre(it)
                }
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


}