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

actual object SqliteExceptionTransformer : DatabaseExceptionTransformer {
    actual override fun transform(throwable: Throwable): DivaDatabaseException {
        return when (throwable) {
            is DivaDatabaseException -> throwable
            else -> mapToDivaException(throwable)
        }
    }

    private fun mapToDivaException(throwable: Throwable): DivaDatabaseException {
        val message = throwable.message ?: ""
        return when {
            message.contains("UNIQUE constraint failed", ignoreCase = true) ->
                UniqueConstraintViolation(message, throwable)
            message.contains("FOREIGN KEY constraint failed", ignoreCase = true) ->
                ForeignKeyConstraintViolation(message, throwable)
            message.contains("NOT NULL constraint failed", ignoreCase = true) ->
                NotNullConstraintViolation(message, throwable)
            message.contains("CHECK constraint failed", ignoreCase = true) ->
                CheckConstraintViolation(message, throwable)
            message.contains("database is locked", ignoreCase = true) ||
                message.contains("database is busy", ignoreCase = true) ->
                DatabaseLockedException(message, throwable)
            message.contains("database disk image is malformed", ignoreCase = true) ->
                DatabaseCorruptionException(message, throwable)
            message.contains("database or disk is full", ignoreCase = true) ->
                DiskFullException(message, throwable)
            message.contains("no such table", ignoreCase = true) ||
                message.contains("has no column named", ignoreCase = true) ->
                DatabaseSchemaException(message, throwable)
            else -> UnknownDatabaseException(message, throwable)
        }
    }
}
