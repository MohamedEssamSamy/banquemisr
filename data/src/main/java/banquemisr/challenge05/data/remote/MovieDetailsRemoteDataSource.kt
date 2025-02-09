package banquemisr.challenge05.data.remote

import banquemisr.challenge05.core.exception.DomainError
import banquemisr.challenge05.core.exception.ErrorType
import banquemisr.challenge05.core.model.Result
import banquemisr.challenge05.data.model.MovieDetailsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class MovieDetailsRemoteDataSource @Inject constructor(private val tmdbApiService: TmdbApiService) {

    fun getMovieDetails(movieId: Long, language: String): Flow<Result<MovieDetailsResponse>> {
        return flow {
            try {
                var response = tmdbApiService.getMovieDetails(movieId, language)
                if (response.isSuccessful) {
                    emit(Result.Success(response.body()!!))
                } else {
                    emit(
                        Result.Error(
                            ErrorType.RemoteError(
                                DomainError.Server(
                                    response.code(),
                                    response.message()
                                )
                            )
                        )
                    )
                }
            } catch (e: IOException) {
                emit(Result.Error(ErrorType.RemoteError(DomainError.Network(e.message ?: ""))))
            } catch (e: Exception) {
                emit(
                    Result.Error(
                        ErrorType.RemoteError(
                            DomainError.Unknown(
                                e.message ?: "Unknown"
                            )
                        )
                    )
                )
            }
        }
    }
}