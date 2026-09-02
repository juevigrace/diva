package io.github.juevigrace.diva.database.sqlite.exception

import android.database.sqlite.SQLiteBindOrColumnIndexOutOfRangeException
import android.database.sqlite.SQLiteCantOpenDatabaseException
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabaseCorruptException
import android.database.sqlite.SQLiteDatabaseLockedException
import android.database.sqlite.SQLiteDatatypeMismatchException
import android.database.sqlite.SQLiteDiskIOException
import android.database.sqlite.SQLiteFullException
import android.database.sqlite.SQLiteTableLockedException
import io.github.juevigrace.diva.database.exception.CheckConstraintViolation
import io.github.juevigrace.diva.database.exception.DatabaseConnectionException
import io.github.juevigrace.diva.database.exception.DatabaseCorruptionException
import io.github.juevigrace.diva.database.exception.DatabaseExceptionTransformer
import io.github.juevigrace.diva.database.exception.DatabaseIOIncomingError
import io.github.juevigrace.diva.database.exception.DatabaseLockedException
import io.github.juevigrace.diva.database.exception.DatabaseQueryException
import io.github.juevigrace.diva.database.exception.DiskFullException
import io.github.juevigrace.diva.database.exception.DivaDatabaseException
import io.github.juevigrace.diva.database.exception.ForeignKeyConstraintViolation
import io.github.juevigrace.diva.database.exception.NotNullConstraintViolation
import io.github.juevigrace.diva.database.exception.UniqueConstraintViolation
import io.github.juevigrace.diva.database.exception.UnknownDatabaseException

actual object SqliteExceptionTransformer : DatabaseExceptionTransformer {
    actual override fun transform(throwable: Throwable): DivaDatabaseException {
        return when (throwable) {
            is DivaDatabaseException -> throwable
            is SQLiteConstraintException -> mapConstraintException(throwable)
            is SQLiteDatabaseCorruptException ->
                DatabaseCorruptionException(throwable.message ?: "", throwable)
            is SQLiteFullException ->
                DiskFullException(throwable.message ?: "", throwable)
            is SQLiteTableLockedException, is SQLiteDatabaseLockedException ->
                DatabaseLockedException(throwable.message ?: "", throwable)
            is SQLiteDiskIOException ->
                DatabaseIOIncomingError(throwable.message ?: "", throwable)
            is SQLiteCantOpenDatabaseException ->
                DatabaseConnectionException(throwable.message ?: "", throwable)
            is SQLiteBindOrColumnIndexOutOfRangeException, is SQLiteDatatypeMismatchException ->
                DatabaseQueryException(throwable.message ?: "", throwable)
            else -> UnknownDatabaseException(throwable.message ?: "Unknown Android SQLite error", throwable)
        }
    }

    private fun mapConstraintException(exception: SQLiteConstraintException): DivaDatabaseException {
        val message = exception.message ?: ""
        return when {
            message.contains("UNIQUE", ignoreCase = true) ->
                UniqueConstraintViolation(message, exception)
            message.contains("FOREIGN KEY", ignoreCase = true) ->
                ForeignKeyConstraintViolation(message, exception)
            message.contains("NOT NULL", ignoreCase = true) ->
                NotNullConstraintViolation(message, exception)
            message.contains("CHECK", ignoreCase = true) ->
                CheckConstraintViolation(message, exception)
            else -> UniqueConstraintViolation(message, exception)
        }
    }
}
