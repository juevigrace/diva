package io.github.juevigrace.diva.database.postgres.exception

import io.github.juevigrace.diva.database.exception.CheckConstraintViolation
import io.github.juevigrace.diva.database.exception.DatabaseConnectionException
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

private const val UNIQUE_VIOLATION = "23505"
private const val FOREIGN_KEY_VIOLATION = "23503"
private const val NOT_NULL_VIOLATION = "23502"
private const val CHECK_VIOLATION = "23514"
private const val DEADLOCK_DETECTED = "40P01"
private const val LOCK_NOT_AVAILABLE = "55P03"
private const val ADMIN_SHUTDOWN = "57P01"
private const val CRASH_SHUTDOWN = "57P02"
private const val CANNOT_CONNECT_NOW = "57P03"

object PostgresExceptionTransformer : DatabaseExceptionTransformer {
    override fun transform(throwable: Throwable): DivaDatabaseException {
        return when (throwable) {
            is DivaDatabaseException -> throwable
            is SQLException -> mapSqlException(throwable)
            else -> UnknownDatabaseException(throwable.message ?: "Unknown Postgres error", throwable)
        }
    }

    private fun mapSqlException(exception: SQLException): DivaDatabaseException {
        val message = exception.message ?: ""
        return when (exception.sqlState) {
            UNIQUE_VIOLATION -> UniqueConstraintViolation(message, exception)
            FOREIGN_KEY_VIOLATION -> ForeignKeyConstraintViolation(message, exception)
            NOT_NULL_VIOLATION -> NotNullConstraintViolation(message, exception)
            CHECK_VIOLATION -> CheckConstraintViolation(message, exception)
            DEADLOCK_DETECTED, LOCK_NOT_AVAILABLE -> DatabaseLockedException(message, exception)
            ADMIN_SHUTDOWN, CRASH_SHUTDOWN, CANNOT_CONNECT_NOW -> DatabaseConnectionException(message, exception)
            else -> {
                when {
                    message.contains("disk full", ignoreCase = true) ->
                        DiskFullException(message, exception)
                    message.contains("does not exist", ignoreCase = true) &&
                        (
                            message.contains("table", ignoreCase = true) ||
                                message.contains("column", ignoreCase = true)
                            ) ->
                        DatabaseSchemaException(message, exception)
                    else -> UnknownDatabaseException(message, exception)
                }
            }
        }
    }
}
