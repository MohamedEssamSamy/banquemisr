package banquemisr.challenge05.core.exception


sealed class ErrorType {
    data class RemoteError(val error: DomainError) : ErrorType()
    data class LocalDatabaseError(val error: DatabaseError) : ErrorType()
}