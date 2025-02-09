package banquemisr.challenge05.data.repository

import banquemisr.challenge05.core.exception.EmitDatabaseError
import banquemisr.challenge05.core.model.Result
import banquemisr.challenge05.core.model.Movie
import banquemisr.challenge05.core.repository.MoviesRepository
import banquemisr.challenge05.data.database.PopularMoviesDao
import banquemisr.challenge05.data.entity.NowPlayingMovieEntity
import banquemisr.challenge05.data.entity.PopularMovieEntity
import banquemisr.challenge05.data.mapper.MoviesMapper
import banquemisr.challenge05.data.remote.PopularMoviesRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PopularMoviesRepositoryImp @Inject constructor(
    private val popularMoviesRemoteDataSource: PopularMoviesRemoteDataSource,
    private val popularMoviesDao: PopularMoviesDao
) :
    MoviesRepository {

    override fun getMoviesFromRemote(language: String, page: Int): Flow<Result<List<Movie>>> {
        return flow {
            popularMoviesRemoteDataSource.getPopularMovies(language, page).collect {
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
                for (movieEntity in popularMoviesDao.getAllMovies()) {
                    var movie: Movie = MoviesMapper.fromPopularMovieEntityToMovie(movieEntity)
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
                    var popularMovieEntity: PopularMovieEntity = MoviesMapper.fromMovieToPopularMovieEntity(movie)
                    popularMoviesDao.insertNewMovie(
                        popularMovieEntity
                    )
                }
            } catch (e: Exception) {
                EmitDatabaseError.emitInsertMoviesDatabaseError(this,e)
            }
        }
    }

}