package banquemisr.challenge05.data.repository

import banquemisr.challenge05.core.exception.DatabaseError
import banquemisr.challenge05.core.exception.EmitDatabaseError
import banquemisr.challenge05.core.exception.ErrorType
import banquemisr.challenge05.core.model.Result
import banquemisr.challenge05.core.model.Movie
import banquemisr.challenge05.core.repository.MoviesRepository
import banquemisr.challenge05.data.database.NowPlayingMoviesDao
import banquemisr.challenge05.data.entity.NowPlayingMovieEntity
import banquemisr.challenge05.data.mapper.MoviesMapper
import banquemisr.challenge05.data.remote.NowPlayingMoviesRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NowPlayingMoviesRepositoryImp @Inject constructor(
    private val nowPlayingMoviesRemoteDataSource: NowPlayingMoviesRemoteDataSource,
    private val nowPlayingMoviesDao: NowPlayingMoviesDao
) :
    MoviesRepository {
    override fun getMoviesFromRemote(language: String, page: Int): Flow<Result<List<Movie>>> {
        return flow {
            nowPlayingMoviesRemoteDataSource.getNowPlayingMovies(language, page).collect {
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
                for (movieEntity in nowPlayingMoviesDao.getAllMovies()) {
                    var movie: Movie = MoviesMapper.fromMovieEntityToMovie(movieEntity)
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
                   var nowPlayingMovieEntity: NowPlayingMovieEntity =
                       MoviesMapper.fromMovieToMovieEntity(movie)
                   nowPlayingMoviesDao.insertNewMovie(
                       nowPlayingMovieEntity
                   )
               }
           } catch (e: Exception) {
               EmitDatabaseError.emitInsertMoviesDatabaseError(this,e)
           }
       }
    }
}