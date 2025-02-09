package banquemisr.challenge05.core.model

import banquemisr.challenge05.core.exception.ErrorType

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val error: ErrorType) : Result<Nothing>()
}
