package banquemisr.challenge05.core.repository

import banquemisr.challenge05.core.model.Result
import banquemisr.challenge05.core.model.Movie
import kotlinx.coroutines.flow.Flow


interface MoviesRepository {
     fun getMoviesFromRemote(language: String, page: Int): Flow<Result<List<Movie>>>
     fun getAllMoviesFromDatabase(): Flow<Result<List<Movie>>>
     suspend fun insertNewMovieInsideDatabase(movie:List<Movie>): Flow<Result<Nothing>>
}