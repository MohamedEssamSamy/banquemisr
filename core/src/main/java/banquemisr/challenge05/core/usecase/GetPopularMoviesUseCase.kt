package banquemisr.challenge05.core.usecase

import banquemisr.challenge05.core.exception.EmitDatabaseError
import banquemisr.challenge05.core.model.Result
import banquemisr.challenge05.core.model.Movie
import banquemisr.challenge05.core.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Named

class GetPopularMoviesUseCase @Inject constructor(@Named("PopularMoviesRepositoryImp") private val popularMoviesRepository: MoviesRepository) {

    operator fun invoke(language: String, page: Int): Flow<Result<List<Movie>>> {
        return flow {
            popularMoviesRepository.getMoviesFromRemote(language, page).collect {
                var response = it
                when (response) {
                    is Result.Success -> {
                        emit(response)
                        insertPopularMoviesInsideDatabase(response.data, this)
                    }

                    is Result.Error -> {
                        emitPopularMoviesFromDatabase(response, this)
                    }
                }
            }
        }
    }

    private suspend fun insertPopularMoviesInsideDatabase(
        list: List<Movie>,
        flow: FlowCollector<Result<Nothing>>
    ) {
        try {
            popularMoviesRepository
                .insertNewMovieInsideDatabase(list)
                .collect {}
        } catch (e: Exception) {
            EmitDatabaseError.emitInsertMoviesDatabaseError(flow, e)
        }
    }

    private suspend fun emitPopularMoviesFromDatabase(
        response: Result.Error,
        flow: FlowCollector<Result<List<Movie>>>
    ) {
        try {
            popularMoviesRepository
                .getAllMoviesFromDatabase()
                .collect {
                    flow.emit(it)
                    flow.emit(Result.Error(response.error))
                }
        } catch (e: Exception) {
            flow.emit(Result.Error(response.error))
            EmitDatabaseError.emitRetrieveMoviesDatabaseError(flow, e)
        }
    }
}