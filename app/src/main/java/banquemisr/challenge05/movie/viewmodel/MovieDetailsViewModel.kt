package banquemisr.challenge05.movie.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import banquemisr.challenge05.core.exception.DatabaseError
import banquemisr.challenge05.core.exception.DomainError
import banquemisr.challenge05.core.exception.ErrorType
import banquemisr.challenge05.core.model.Movie
import banquemisr.challenge05.core.model.MovieDetails
import banquemisr.challenge05.core.model.Result
import banquemisr.challenge05.core.usecase.GetMovieDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(private val getMovieDetailsUseCase: GetMovieDetailsUseCase) :
    ViewModel() {
    private val TAG = "MovieDetailsViewModelTAG"

    public var movieDetailsStateFlow: MutableStateFlow<MovieDetails> = MutableStateFlow(
        MovieDetails(
            0, "", "", "",
            emptyList(), "", 0
        )
    )
        private set(value) {}

    public var errorMessage: MutableSharedFlow<String> = MutableSharedFlow(0)
        private set(value) {}

    fun fetchMovieDetails(movieId: Long) {
        movieDetailsStateFlow.value = MovieDetails(0, "", "", "", emptyList(), "", 0)
        viewModelScope.launch(Dispatchers.IO) {
            getMovieDetailsUseCase.invoke(movieId, "en-US")
                .collect {
                    var response = it
                    when (response) {
                        is Result.Success -> {
                            movieDetailsStateFlow.value = response.data
                        }

                        is Result.Error -> {
                            handleError(response.error)
                        }
                    }
                }
        }
    }

    private suspend fun handleError(error: ErrorType) {
        when (error) {
            is ErrorType.RemoteError -> handleDomainError(error.error)
            is ErrorType.LocalDatabaseError -> {
                handleLocalDatabaseError(error.error)
            }
        }
    }

    private suspend fun handleDomainError(error: DomainError) {
        when (error) {
            is DomainError.Server -> errorMessage.emit("Server Error: Code ${error.code}, Message: ${error.message}")

            is DomainError.Network -> errorMessage.emit("Network Error: ${error.message}")

            is DomainError.Unknown -> errorMessage.emit("Unknown Error: ${error.message}")
        }
    }

    private suspend fun handleLocalDatabaseError(error: DatabaseError) {
        when (error) {
            is DatabaseError.CancellationException -> errorMessage.emit("Database Error: ${error.message}")

            is DatabaseError.IOException -> errorMessage.emit("Database Error: ${error.message}")


            is DatabaseError.IllegalStateException -> errorMessage.emit("Database Error: ${error.message}")

            is DatabaseError.RuntimeException -> errorMessage.emit("Database Error: ${error.message}")

            is DatabaseError.SQLiteAbortException -> errorMessage.emit("Database Error: ${error.message}")

            is DatabaseError.SQLiteDatabaseCorruptException -> errorMessage.emit("Database Error: ${error.message}")

            is DatabaseError.SQLiteFullException -> errorMessage.emit("Database Error: ${error.message}")
            is DatabaseError.TransactionException -> errorMessage.emit("Database Error: ${error.message}")

            is DatabaseError.TypeConversionException -> errorMessage.emit("Database Error: ${error.message}")
        }
    }
}