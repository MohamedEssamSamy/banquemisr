package banquemisr.challenge05.data.repository

import banquemisr.challenge05.core.exception.EmitDatabaseError
import banquemisr.challenge05.core.model.Result
import banquemisr.challenge05.core.model.Movie
import banquemisr.challenge05.core.repository.MoviesRepository
import banquemisr.challenge05.data.database.UpcomingMoviesDao
import banquemisr.challenge05.data.entity.PopularMovieEntity
import banquemisr.challenge05.data.entity.UpcomingMovieEntity
import banquemisr.challenge05.data.mapper.MoviesMapper
import banquemisr.challenge05.data.remote.UpcomingMoviesRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpcomingMoviesRepositoryImp @Inject constructor(
    private val upcomingMoviesRemoteDataSource: UpcomingMoviesRemoteDataSource,
    private val upcomingMoviesDao: UpcomingMoviesDao
) :
    MoviesRepository {
    override fun getMoviesFromRemote(language: String, page: Int): Flow<Result<List<Movie>>> {
        return flow {
            upcomingMoviesRemoteDataSource.getUpcomingMovies(language, page).collect {
                var response = it

                when (response) {
                    is Result.Success -> emit(Result.Success(response.data.results.map {
                        MoviesMapper.fromMovieModelToMovie(
                            it
                        )
                    }))

                    is Result.Error -> emit(Result.Error(response.error))
                }
            }
        }
    }

    override fun getAllMoviesFromDatabase(): Flow<Result<List<Movie>>> {
        return flow {
            try {
                var moviesList = ArrayList<Movie>()
                for (movieEntity in upcomingMoviesDao.getAllMovies()) {
                    var movie:Movie = MoviesMapper.fromUpcomingMovieEntityToMovie(movieEntity)
                    moviesList.add(movie)
                }
                emit(Result.Success(moviesList))
            } catch (e: Exception) {
                EmitDatabaseError.emitRetrieveMoviesDatabaseError(this,e)
            }
        }
    }

    override suspend fun insertNewMovieInsideDatabase(movies:List<Movie>) : Flow<Result<Nothing>> {
        return flow {
            try {
                for(movie in movies){
                    var upcomingMovieEntity: UpcomingMovieEntity = MoviesMapper.fromMovieToUpcomingMovieEntity(movie)
                    upcomingMoviesDao.insertNewMovie(
                        upcomingMovieEntity
                    )
                }
            } catch (e: Exception) {
                EmitDatabaseError.emitInsertMoviesDatabaseError(this,e)
            }
        }
    }
}