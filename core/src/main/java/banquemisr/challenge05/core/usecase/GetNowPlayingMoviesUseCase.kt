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

class GetNowPlayingMoviesUseCase @Inject constructor(@Named("NowPlayingMoviesRepositoryImp") private val nowPlayingMoviesRepository: MoviesRepository) {
    operator fun invoke(language: String, page: Int): Flow<Result<List<Movie>>> {
        return flow {
            nowPlayingMoviesRepository.getMoviesFromRemote(language, page).collect {
                var response = it
                when (response) {
                    is Result.Success -> {
                        emit(response)
                        insertNewMoviesInsideDatabase(response.data, this)
                    }

                    is Result.Error -> {
                        emitNewMoviesFromDatabase(response, this)
                    }
                }
            }
        }
    }

    private suspend fun insertNewMoviesInsideDatabase(
        list: List<Movie>,
        flow: FlowCollector<Result<Nothing>>
    ) {
        try {
            nowPlayingMoviesRepository
                .insertNewMovieInsideDatabase(list)
                .collect {}
        } catch (e: Exception) {
            EmitDatabaseError.emitInsertMoviesDatabaseError(flow, e)
        }
    }

    private suspend fun emitNewMoviesFromDatabase(
        response: Result.Error,
        flow: FlowCollector<Result<List<Movie>>>
    ) {
        try {
            nowPlayingMoviesRepository
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