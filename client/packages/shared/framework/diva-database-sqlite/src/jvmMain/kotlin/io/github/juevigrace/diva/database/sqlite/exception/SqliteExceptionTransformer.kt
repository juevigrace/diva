package io.github.juevigrace.diva.database.sqlite.exception

import io.github.juevigrace.diva.database.exception.CheckConstraintViolation
import io.github.juevigrace.diva.database.exception.DatabaseCorruptionException
import io.github.juevigrace.diva.database.exception.DatabaseExceptionTransformer
import io.github.juevigrace.diva.database.exception.DatabaseLockedException
import io.github.juevigrace.diva.database.exception.DatabaseSchemaException
import io.github.juevigrace.diva.database.exception.DiskFullException
import io.github.juevigrace.diva.database.exception.DivaDatabaseException
import io.github.juevigrace.diva.database.exception.ForeignKeyConstraintViolation
import io.github.juevigrace.diva.database.exception.NotNullConstraintViolation
import io.github.juevigrace.diva.database.exception.UniqueConstraintViolation
import io.github.juevigrace.diva.database.exception.UnknownDatabaseException
import java.sql.SQLException

actual object SqliteExceptionTransformer : DatabaseExceptionTransformer {
    actual override fun transform(throwable: Throwable): DivaDatabaseException {
        return when (throwable) {
            is DivaDatabaseException -> throwable
            is SQLException -> mapSqlException(throwable)
            else -> UnknownDatabaseException(throwable.message ?: "Unknown SQLite error", throwable)
        }
    }

    private fun mapSqlException(exception: SQLException): DivaDatabaseException {
        val message = exception.message ?: ""
        return when {
            message.contains("UNIQUE constraint failed", ignoreCase = true) ->
                UniqueConstraintViolation(message, exception)
            message.contains("FOREIGN KEY constraint failed", ignoreCase = true) ->
                ForeignKeyConstraintViolation(message, exception)
            message.contains("NOT NULL constraint failed", ignoreCase = true) ->
                NotNullConstraintViolation(message, exception)
            message.contains("CHECK constraint failed", ignoreCase = true) ->
                CheckConstraintViolation(message, exception)
            message.contains("database is locked", ignoreCase = true) ||
                message.contains("database is busy", ignoreCase = true) ->
                DatabaseLockedException(message, exception)
            message.contains("database disk image is malformed", ignoreCase = true) ->
                DatabaseCorruptionException(message, exception)
            message.contains("database or disk is full", ignoreCase = true) ->
                DiskFullException(message, exception)
            message.contains("no such table", ignoreCase = true) ||
                message.contains("has no column named", ignoreCase = true) ->
                DatabaseSchemaException(message, exception)
            else -> UnknownDatabaseException(message, exception)
        }
    }
}
