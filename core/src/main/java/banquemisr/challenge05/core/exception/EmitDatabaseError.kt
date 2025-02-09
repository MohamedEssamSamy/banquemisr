package banquemisr.challenge05.core.exception

import banquemisr.challenge05.core.model.Movie
import banquemisr.challenge05.core.model.MovieDetails
import banquemisr.challenge05.core.model.Result
import kotlinx.coroutines.flow.FlowCollector

object EmitDatabaseError {
    suspend fun emitRetrieveMoviesDatabaseError(flow: FlowCollector<Result<List<Movie>>>, e: Exception) {
        when (e) {
            is java.io.IOException -> {
                flow.emit(
                    Result.Error(
                        ErrorType.LocalDatabaseError(
                            DatabaseError.IOException(
                                e.message ?: "Unknown IO Error"
                            )
                        )
                    )
                )
            }

            is java.util.concurrent.CancellationException -> {
                flow.emit(
                    Result.Error(
                        ErrorType.LocalDatabaseError(
                            DatabaseError.CancellationException(
                                e.message ?: "Operation Cancelled"
                            )
                        )
                    )
                )
            }

            is IllegalStateException -> {
                flow.emit(
                    Result.Error(
                        ErrorType.LocalDatabaseError(
                            DatabaseError.IllegalStateException(
                                e.message ?: "Unknown State Error"
                            )
                        )
                    )
                )
            }

            is ClassCastException -> {
                flow.emit(
                    Result.Error(
                        ErrorType.LocalDatabaseError(
                            DatabaseError.TypeConversionException(
                                e.message ?: "Type Conversion Failed"
                            )
                        )
                    )
                )
            }

            is android.database.sqlite.SQLiteDatabaseCorruptException -> {
                flow.emit(
                    Result.Error(
                        ErrorType.LocalDatabaseError(
                            DatabaseError.SQLiteDatabaseCorruptException(
                                e.message ?: "Database Corrupted"
                            )
                        )
                    )
                )
            }

            is android.database.sqlite.SQLiteFullException -> {
                flow.emit(
                    Result.Error(
                        ErrorType.LocalDatabaseError(
                            DatabaseError.SQLiteFullException(
                                e.message ?: "Database Full"
                            )
                        )
                    )
                )
            }

            is android.database.sqlite.SQLiteAbortException -> {
                flow.emit(
                    Result.Error(
                        ErrorType.LocalDatabaseError(
                            DatabaseError.SQLiteAbortException(
                                e.message ?: "Database Operation Aborted"
                            )
                        )
                    )
                )
            }

            is java.lang.RuntimeException -> {
                flow.emit(
                    Result.Error(
                        ErrorType.LocalDatabaseError(
                            DatabaseError.RuntimeException(
                                e.message ?: "Runtime Exception Occurred"
                            )
                        )
                    )
                )
            }

            is Exception -> {
                flow.emit(
                    Result.Error(
                        ErrorType.LocalDatabaseError(
                            DatabaseError.TransactionException(
                                e.message ?: "Transaction Failed"
                            )
                        )
                    )
                )
            }
        }
    }

    suspend fun emitInsertMoviesDatabaseError(flow: FlowCollector<Result<Nothing>>, e: Exception) {
        when (e) {
            is java.io.IOException -> {
                flow.emit(
                    Result.Error(
                        ErrorType.LocalDatabaseError(
                            DatabaseError.IOException(
                                e.message ?: "Unknown IO Error"
                            )
                        )
                    )
                )
            }

            is java.util.concurrent.CancellationException -> {
                flow.emit(
                    Result.Error(
                        ErrorType.LocalDatabaseError(
                            DatabaseError.CancellationException(
                                e.message ?: "Operation Cancelled"
                            )
                        )
                    )
                )
            }

            is IllegalStateException -> {
                flow.emit(
                    Result.Error(
                        ErrorType.LocalDatabaseError(
                            DatabaseError.IllegalStateException(
                                e.message ?: "Unknown State Error"
                            )
                        )
                    )
                )
            }

            is ClassCastException -> {
                flow.emit(
                    Result.Error(
                        ErrorType.LocalDatabaseError(
                            DatabaseError.TypeConversionException(
                                e.message ?: "Type Conversion Failed"
                            )
                        )
                    )
                )
            }

            is android.database.sqlite.SQLiteDatabaseCorruptException -> {
                flow.emit(
                    Result.Error(
                        ErrorType.LocalDatabaseError(
                            DatabaseError.SQLiteDatabaseCorruptException(
                                e.message ?: "Database Corrupted"
                            )
                        )
                    )
                )
            }

            is android.database.sqlite.SQLiteFullException -> {
                flow.emit(
                    Result.Error(
                        ErrorType.LocalDatabaseError(
                            DatabaseError.SQLiteFullException(
                                e.message ?: "Database Full"
                            )
                        )
                    )
                )
            }

            is android.database.sqlite.SQLiteAbortException -> {
                flow.emit(
                    Result.Error(
                        ErrorType.LocalDatabaseError(
                            DatabaseError.SQLiteAbortException(
                                e.message ?: "Database Operation Aborted"
                            )
                        )
                    )
                )
            }

            is java.lang.RuntimeException -> {
                flow.emit(
                    Result.Error(
                        ErrorType.LocalDatabaseError(
                            DatabaseError.RuntimeException(
                                e.message ?: "Runtime Exception Occurred"
                            )
                        )
                    )
                )
            }

            is Exception -> {
                flow.emit(
                    Result.Error(
                        ErrorType.LocalDatabaseError(
                            DatabaseError.TransactionException(
                                e.message ?: "Transaction Failed"
                            )
                        )
                    )
                )
            }
        }
    }

    suspend fun emitRetrieveMovieDetailsDatabaseError(flow: FlowCollector<Result<MovieDetails>>, e: Exception) {
        when (e) {
            is java.io.IOException -> {
                flow.emit(
                    Result.Error(
                        ErrorType.LocalDatabaseError(
                            DatabaseError.IOException(
                                e.message ?: "Unknown IO Error"
                            )
                        )
                    )
                )
            }

            is java.util.concurrent.CancellationException -> {
                flow.emit(
                    Result.Error(
                        ErrorType.LocalDatabaseError(
                            DatabaseError.CancellationException(
                                e.message ?: "Operation Cancelled"
                            )
                        )
                    )
                )
            }

            is IllegalStateException -> {
                flow.emit(
                    Result.Error(
                        ErrorType.LocalDatabaseError(
                            DatabaseError.IllegalStateException(
                                e.message ?: "Unknown State Error"
                            )
                        )
                    )
                )
            }

            is ClassCastException -> {
                flow.emit(
                    Result.Error(
                        ErrorType.LocalDatabaseError(
                            DatabaseError.TypeConversionException(
                                e.message ?: "Type Conversion Failed"
                            )
                        )
                    )
                )
            }

            is android.database.sqlite.SQLiteDatabaseCorruptException -> {
                flow.emit(
                    Result.Error(
                        ErrorType.LocalDatabaseError(
                            DatabaseError.SQLiteDatabaseCorruptException(
                                e.message ?: "Database Corrupted"
                            )
                        )
                    )
                )
            }

            is android.database.sqlite.SQLiteFullException -> {
                flow.emit(
                    Result.Error(
                        ErrorType.LocalDatabaseError(
                            DatabaseError.SQLiteFullException(
                                e.message ?: "Database Full"
                            )
                        )
                    )
                )
            }

            is android.database.sqlite.SQLiteAbortException -> {
                flow.emit(
                    Result.Error(
                        ErrorType.LocalDatabaseError(
                            DatabaseError.SQLiteAbortException(
                                e.message ?: "Database Operation Aborted"
                            )
                        )
                    )
                )
            }

            is java.lang.RuntimeException -> {
                flow.emit(
                    Result.Error(
                        ErrorType.LocalDatabaseError(
                            DatabaseError.RuntimeException(
                                e.message ?: "Runtime Exception Occurred"
                            )
                        )
                    )
                )
            }

            is Exception -> {
                flow.emit(
                    Result.Error(
                        ErrorType.LocalDatabaseError(
                            DatabaseError.TransactionException(
                                e.message ?: "Transaction Failed"
                            )
                        )
                    )
                )
            }
        }
    }

    suspend fun emitInsertMovieDetailsDatabaseError(flow: FlowCollector<Result<Nothing>>, e: Exception) {
        when (e) {
            is java.io.IOException -> {
                flow.emit(
                    Result.Error(
                        ErrorType.LocalDatabaseError(
                            DatabaseError.IOException(
                                e.message ?: "Unknown IO Error"
                            )
                        )
                    )
                )
            }

            is java.util.concurrent.CancellationException -> {
                flow.emit(
                    Result.Error(
                        ErrorType.LocalDatabaseError(
                            DatabaseError.CancellationException(
                                e.message ?: "Operation Cancelled"
                            )
                        )
                    )
                )
            }

            is IllegalStateException -> {
                flow.emit(
                    Result.Error(
                        ErrorType.LocalDatabaseError(
                            DatabaseError.IllegalStateException(
                                e.message ?: "Unknown State Error"
                            )
                        )
                    )
                )
            }

            is ClassCastException -> {
                flow.emit(
                    Result.Error(
                        ErrorType.LocalDatabaseError(
                            DatabaseError.TypeConversionException(
                                e.message ?: "Type Conversion Failed"
                            )
                        )
                    )
                )
            }

            is android.database.sqlite.SQLiteDatabaseCorruptException -> {
                flow.emit(
                    Result.Error(
                        ErrorType.LocalDatabaseError(
                            DatabaseError.SQLiteDatabaseCorruptException(
                                e.message ?: "Database Corrupted"
                            )
                        )
                    )
                )
            }

            is android.database.sqlite.SQLiteFullException -> {
                flow.emit(
                    Result.Error(
                        ErrorType.LocalDatabaseError(
                            DatabaseError.SQLiteFullException(
                                e.message ?: "Database Full"
                            )
                        )
                    )
                )
            }

            is android.database.sqlite.SQLiteAbortException -> {
                flow.emit(
                    Result.Error(
                        ErrorType.LocalDatabaseError(
                            DatabaseError.SQLiteAbortException(
                                e.message ?: "Database Operation Aborted"
                            )
                        )
                    )
                )
            }

            is java.lang.RuntimeException -> {
                flow.emit(
                    Result.Error(
                        ErrorType.LocalDatabaseError(
                            DatabaseError.RuntimeException(
                                e.message ?: "Runtime Exception Occurred"
                            )
                        )
                    )
                )
            }

            is Exception -> {
                flow.emit(
                    Result.Error(
                        ErrorType.LocalDatabaseError(
                            DatabaseError.TransactionException(
                                e.message ?: "Transaction Failed"
                            )
                        )
                    )
                )
            }
        }
    }

}