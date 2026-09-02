package io.github.juevigrace.diva.database.mysql.exception

import io.github.juevigrace.diva.database.exception.CheckConstraintViolation
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

private const val DUPLICATE_KEY = 1062
private const val LOCK_WAIT_TIMEOUT = 1205
private const val DEADLOCK_FOUND = 1213
private const val NOT_NULL_VIOLATION = 1048
private const val CHECK_VIOLATION = 3819
private const val FOREIGN_KEY_CHILD_ROW = 1451
private const val FOREIGN_KEY_PARENT_ROW = 1452
private const val FOREIGN_KEY_CONSTRAINT_FAIL_1 = 1216
private const val FOREIGN_KEY_CONSTRAINT_FAIL_2 = 1217

object MysqlExceptionTransformer : DatabaseExceptionTransformer {
    override fun transform(throwable: Throwable): DivaDatabaseException {
        return when (throwable) {
            is DivaDatabaseException -> throwable
            is SQLException -> mapSqlException(throwable)
            else -> UnknownDatabaseException(throwable.message ?: "Unknown MySQL error", throwable)
        }
    }

    private fun mapSqlException(exception: SQLException): DivaDatabaseException {
        val message = exception.message ?: ""
        return when (exception.errorCode) {
            DUPLICATE_KEY -> UniqueConstraintViolation(message, exception)
            FOREIGN_KEY_CONSTRAINT_FAIL_1,
            FOREIGN_KEY_CONSTRAINT_FAIL_2,
            FOREIGN_KEY_CHILD_ROW,
            FOREIGN_KEY_PARENT_ROW -> ForeignKeyConstraintViolation(message, exception)
            NOT_NULL_VIOLATION -> NotNullConstraintViolation(message, exception)
            CHECK_VIOLATION -> CheckConstraintViolation(message, exception)
            LOCK_WAIT_TIMEOUT, DEADLOCK_FOUND -> DatabaseLockedException(message, exception)
            else -> {
                when {
                    message.contains("database or disk is full", ignoreCase = true) ->
                        DiskFullException(message, exception)
                    message.contains("no such table", ignoreCase = true) ||
                        message.contains("Unknown column", ignoreCase = true) ->
                        DatabaseSchemaException(message, exception)
                    else -> UnknownDatabaseException(message, exception)
                }
            }
        }
    }
}
