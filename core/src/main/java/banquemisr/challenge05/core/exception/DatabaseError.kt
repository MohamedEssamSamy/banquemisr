package banquemisr.challenge05.core.exception

sealed class DatabaseError {
    data class IOException(var message: String) : DatabaseError()
    data class IllegalStateException(var message: String) : DatabaseError()
    data class TypeConversionException(var message: String) : DatabaseError()
    data class SQLiteDatabaseCorruptException(var message: String) : DatabaseError()
    data class SQLiteFullException(var message: String) : DatabaseError()
    data class SQLiteAbortException(var message: String) : DatabaseError()
    data class RuntimeException(var message: String) : DatabaseError()
    data class CancellationException(var message: String) : DatabaseError()
    data class TransactionException(var message: String) : DatabaseError()
}