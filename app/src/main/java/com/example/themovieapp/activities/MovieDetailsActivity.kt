package com.example.themovieapp.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager.LayoutParams.*
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.themovieapp.R
import com.example.themovieapp.data.vos.GenreVO
import com.example.themovieapp.data.vos.MovieVO
import com.example.themovieapp.mvvm.MovieDetailsViewModel
import com.example.themovieapp.utils.IMAGE_BASE_URL
import com.example.themovieapp.viewpods.ActorListViewPod
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_movie_details.*
import kotlinx.android.synthetic.main.view_holder_movie.*

class MovieDetailsActivity : AppCompatActivity() {
    //View Pods
    lateinit var actorsViewPod: ActorListViewPod;
    lateinit var creatorsViewPod: ActorListViewPod;

    //ViewModel
    private lateinit var mViewModel: MovieDetailsViewModel

    companion object {
        private const val EXTRA_MOVIE_ID = "EXTRA_MOVIE_ID"
        fun newIntent(context: Context, movieId: Int): Intent {
            val intent = Intent(context, MovieDetailsActivity::class.java)
            intent.putExtra(EXTRA_MOVIE_ID, movieId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        window.setFlags(
            FLAG_LAYOUT_NO_LIMITS,
            FLAG_LAYOUT_NO_LIMITS
        )

        val movieId = intent?.getIntExtra(EXTRA_MOVIE_ID, 0)
        movieId?.let {
            setUpViewModel(it)
        }

        setUpViewPods()
        setUpListeners()
        observeLiveData()

    }

    private fun setUpViewModel(movieId: Int) {
        mViewModel = ViewModelProvider(this).get(MovieDetailsViewModel::class.java)
        mViewModel.getInitialData(movieId)
    }

    private fun observeLiveData() {
        mViewModel.movieDetailsLiveData?.observe(this) {
            it?.let { movie -> bindData(movie) }
        }
        mViewModel.castLiveData.observe(this,actorsViewPod::setData)
        mViewModel.crewLiveData.observe(this,creatorsViewPod::setData)
    }


    private fun setUpListeners() {
        btnBack.setOnClickListener {
            super.onBackPressed()
        }
    }

    private fun setUpViewPods() {
        actorsViewPod = vpActors as ActorListViewPod
        actorsViewPod.setUpActorViewPod(
            backgroundColorReference = R.color.colorPrimary,
            titleText = getString(R.string.lbl_actors),
            moreTitleText = ""
        )
        creatorsViewPod = vpCreators as ActorListViewPod
        creatorsViewPod.setUpActorViewPod(
            backgroundColorReference = R.color.colorPrimary,
            titleText = getString(R.string.lbl_creators),
            moreTitleText = getString(R.string.lbl_more_creators)
        )
    }

    private fun bindData(movie: MovieVO) {
        Glide.with(this)
            .load("$IMAGE_BASE_URL${movie.posterPath}")
            .into(ivMovieDetails)
        tvMovieDetailName.text = movie.title ?: ""
        tvMovieReleaseYear.text = movie.releaseDate?.substring(0, 4)
        tvRating.text = movie.voteAverage?.toString() ?: ""
        movie.voteCount?.let {
            tvNumberOfVotes.text = "$it VOTES"
        }
        rbRatingMovieDetails.rating = movie.getRatingBaseOnFiveStars()
        bindGenres(movie, movie.genres ?: listOf())
        tvOverview.text = movie.overView ?: ""
        tvOriginalTitle.text = movie.title ?: ""
        tvType.text = movie.getGeneresAsCommaSeparatedString()
        tvProduction.text = movie.getProductionCountriesAsCommaSeparatedString()
        tvPremiere.text = movie.releaseDate ?: ""
        tvDescription.text = movie.overView ?: ""
    }

    private fun bindGenres(
        movie: MovieVO,
        genres: List<GenreVO>
    ) {
        movie.genres?.count()?.let {
            tvFirstGenre.text = genres.firstOrNull()?.name ?: ""
            tvSecondGenre.text = genres.getOrNull(1)?.name ?: ""
            tvThirdGenre.text = genres.getOrNull(2)?.name ?: ""

            if (it < 3) {
                tvThirdGenre.visibility = View.GONE
            } else if (it < 2) {
                tvSecondGenre.visibility = View.GONE
            }
        }
    }

    private fun showError(message: String) {
        Snackbar.make(
            window.decorView,
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }

}