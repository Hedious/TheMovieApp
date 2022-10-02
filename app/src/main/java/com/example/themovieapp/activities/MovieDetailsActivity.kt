package com.example.themovieapp.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.example.themovieapp.R
import com.example.themovieapp.data.models.MovieModel
import com.example.themovieapp.data.models.MovieModelImpl
import com.example.themovieapp.data.vos.GenreVO
import com.example.themovieapp.data.vos.MovieVO
import com.example.themovieapp.utils.IMAGE_BASE_URL
import com.example.themovieapp.viewpods.ActorListViewPod
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_movie_details.*
import kotlinx.android.synthetic.main.view_holder_movie.*

class MovieDetailsActivity : AppCompatActivity() {
    //View Pods
    lateinit var actorsViewPod: ActorListViewPod;
    lateinit var creatorsViewPod: ActorListViewPod;
    private val mMovieModel: MovieModel = MovieModelImpl

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

        setUpViewPods()
        setUpListeners()

        val movieId = intent?.getIntExtra(EXTRA_MOVIE_ID, 0)
        movieId?.let {
            requestData(it)
        }
    }

    private fun requestData(movieId: Int){
        mMovieModel.getMovieDetails(
            movieId = movieId.toString(),
            onFailure = {showError(it)}
        )?.observe(this, {
            it?.let { movieDetails -> bindData(movieDetails) }
        })

        mMovieModel.getCreditsByMovie(
            movieId = movieId.toString(),
            onSuccess = {
                actorsViewPod.setData(it.first)
                creatorsViewPod.setData(it.second)
            },
            onFailure = {
                showError(it)
            }
        )
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

    private fun bindData(movie: MovieVO){
        Glide.with(this)
            .load("$IMAGE_BASE_URL${movie.posterPath}")
            .into(ivMovieDetails)
            tvMovieDetailName.text = movie.title ?: ""
            tvMovieReleaseYear.text = movie.releaseDate?.substring(0,4)
            tvRating.text = movie.voteAverage?.toString() ?: ""
            movie.voteCount?.let {
                tvNumberOfVotes.text = "$it VOTES"
            }
            rbRatingMovieDetails.rating = movie.getRatingBaseOnFiveStars()
            bindGenres(movie,movie.genres ?: listOf())
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
    ){
        movie.genres?.count()?.let {
            tvFirstGenre.text = genres.firstOrNull()?.name ?: ""
            tvSecondGenre.text = genres.getOrNull(1)?.name ?: ""
            tvThirdGenre.text = genres.getOrNull(2)?.name ?: ""

            if(it < 3) {
                tvThirdGenre.visibility = View.GONE
            }else if(it < 2) {
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