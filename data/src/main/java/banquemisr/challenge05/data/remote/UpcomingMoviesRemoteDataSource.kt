package banquemisr.challenge05.data.remote

import banquemisr.challenge05.core.exception.DomainError
import banquemisr.challenge05.core.exception.ErrorType
import banquemisr.challenge05.core.model.Result
import banquemisr.challenge05.data.model.MoviesResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class UpcomingMoviesRemoteDataSource @Inject constructor(private val tmdbApiService: TmdbApiService) {
     fun getUpcomingMovies(language: String, page: Int): Flow<Result<MoviesResponse>> {
        return flow {
            try {
                var response = tmdbApiService.getUpcomingMovies(language, page)
                if (response.isSuccessful) {
                    emit(Result.Success(response.body()!!))
                } else {
                    emit(Result.Error(ErrorType.RemoteError(DomainError.Server(response.code(), response.message()))))
                }
            } catch (e: IOException) {
                emit(Result.Error(ErrorType.RemoteError(DomainError.Network(e.message ?: ""))))
            } catch (e: Exception) {
                emit(Result.Error(ErrorType.RemoteError(DomainError.Unknown(e.message ?: "Unknown"))))
            }
        }
    }
}