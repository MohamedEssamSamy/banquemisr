package banquemisr.challenge05.data.repository

import banquemisr.challenge05.core.exception.EmitDatabaseError
import banquemisr.challenge05.core.model.MovieDetails
import banquemisr.challenge05.core.repository.MovieDetailsRepository
import banquemisr.challenge05.data.mapper.MovieDetailsMapper
import banquemisr.challenge05.data.remote.MovieDetailsRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import banquemisr.challenge05.core.model.Result
import banquemisr.challenge05.data.database.MovieDetailsDao

class MovieDetailsRepositoryImp @Inject constructor(
    private val movieDetailsRemoteDataSource: MovieDetailsRemoteDataSource,
    private val movieDetailsDao: MovieDetailsDao
) :
    MovieDetailsRepository {
    override suspend fun getMovieDetails(
        movieId: Long,
        language: String
    ): Flow<Result<MovieDetails>> {
        return flow {
            movieDetailsRemoteDataSource.getMovieDetails(movieId, language)
                .collect {
                    var response = it
                    when (response) {
                        is Result.Success -> {
                            emit(Result.Success(MovieDetailsMapper.toMovieDetails(response.data)))
                        }

                        is Result.Error -> emit(Result.Error(response.error))
                    }
                }
        }
    }


    override fun getMovieDetailsFromDatabase(id: Long): Flow<Result<MovieDetails>> {
        return flow {
            try {
                var movieDetails: MovieDetails =
                    MovieDetailsMapper.fromMovieDetailsEntityToMovieDetails(
                        movieDetailsDao.getMovieDetails(id)!!
                    )
                emit(Result.Success(movieDetails))
            } catch (e: Exception) {
                EmitDatabaseError.emitRetrieveMovieDetailsDatabaseError(this, e)
            }

        }
    }

    override suspend fun insertNewMovieDetailsInsideDatabase(movieDetails: MovieDetails): Flow<Result<Nothing>> {
        return flow {
            try {
                movieDetailsDao.insertNewMovie(
                    MovieDetailsMapper.fromMovieDetailsToMovieDetailsEntity(
                        movieDetails
                    )
                )
            } catch (e: Exception) {
                EmitDatabaseError.emitInsertMoviesDatabaseError(this, e)
            }
        }
    }
}