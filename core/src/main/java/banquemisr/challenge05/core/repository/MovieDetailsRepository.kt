package banquemisr.challenge05.core.repository

import banquemisr.challenge05.core.model.Movie
import banquemisr.challenge05.core.model.MovieDetails
import kotlinx.coroutines.flow.Flow
import banquemisr.challenge05.core.model.Result

interface MovieDetailsRepository {
    suspend fun getMovieDetails(movieId: Long, language: String): Flow<Result<MovieDetails>>
    fun getMovieDetailsFromDatabase(id:Long): Flow<Result<MovieDetails>>
    suspend fun insertNewMovieDetailsInsideDatabase(movieDetails:MovieDetails): Flow<Result<Nothing>>
}