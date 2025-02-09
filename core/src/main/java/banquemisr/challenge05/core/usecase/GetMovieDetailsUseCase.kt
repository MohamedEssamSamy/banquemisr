package banquemisr.challenge05.core.usecase

import banquemisr.challenge05.core.exception.EmitDatabaseError
import banquemisr.challenge05.core.model.MovieDetails
import banquemisr.challenge05.core.model.Result
import banquemisr.challenge05.core.repository.MovieDetailsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(private val movieDetailsRepository: MovieDetailsRepository) {
    operator fun invoke(movieId: Long, language: String): Flow<Result<MovieDetails>> {

        return flow {
            movieDetailsRepository.getMovieDetails(movieId, language).collect {
                var response = it
                when (response) {
                    is Result.Success -> {
                        emit(response)
                        insertMovieDetailsInsideDatabase(response.data, this)
                    }

                    is Result.Error -> {
                        emitMovieDetailsFromDatabase(movieId, response, this)
                    }
                }
            }
        }
    }

    private suspend fun insertMovieDetailsInsideDatabase(
        movieDetails: MovieDetails,
        flow: FlowCollector<Result<MovieDetails>>
    ) {
        try {
            movieDetailsRepository
                .insertNewMovieDetailsInsideDatabase(movieDetails)
                .collect {}
        } catch (e: Exception) {
            EmitDatabaseError.emitInsertMovieDetailsDatabaseError(flow, e)
        }
    }

    private suspend fun emitMovieDetailsFromDatabase(
        movieId: Long,
        response: Result.Error,
        flow: FlowCollector<Result<MovieDetails>>
    ) {
        try {
            movieDetailsRepository
                .getMovieDetailsFromDatabase(movieId)
                .collect {
                    flow.emit(it)
                    flow.emit(Result.Error(response.error))
                }
        } catch (e: Exception) {
            flow.emit(Result.Error(response.error))
            EmitDatabaseError.emitRetrieveMovieDetailsDatabaseError(flow, e)
        }
    }
}