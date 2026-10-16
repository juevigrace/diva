package io.github.juevigrace.diva.database.test

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import io.github.juevigrace.diva.database.driver.DriverProvider
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
import io.github.juevigrace.diva.database.sqlite.config.SqliteConf
import io.github.juevigrace.diva.database.sqlite.exception.SqliteExceptionTransformer
import kotlinx.coroutines.CancellationException
import java.sql.SQLException
import java.util.Properties

actual val provider: DriverProvider<SqliteConf> = object : DriverProvider<SqliteConf> {
    override val conf = SqliteConf("test", inMemory = true, foreignKeys = true)

    // JdbcSqliteDriver throws java.sql.SQLException, which the androidMain
    // SqliteExceptionTransformer does not recognize, so map it by message like jvmMain.
    override val transformer: DatabaseExceptionTransformer = object : DatabaseExceptionTransformer {
        override fun transform(throwable: Throwable): DivaDatabaseException {
            if (throwable !is SQLException) {
                return SqliteExceptionTransformer.transform(throwable)
            }
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

    private fun properties(): Properties = Properties().apply {
        if (conf.foreignKeys) put("foreign_keys", "true")
    }

    override fun createSyncDriver(schema: SqlSchema<QueryResult.Value<Unit>>): Result<SqlDriver> {
        return runCatching {
            JdbcSqliteDriver(
                url = JdbcSqliteDriver.IN_MEMORY,
                properties = properties(),
                schema = schema,
            )
        }
    }

    override suspend fun createAsyncDriver(schema: SqlSchema<QueryResult.AsyncValue<Unit>>): Result<SqlDriver> {
        return runCatching {
            JdbcSqliteDriver(
                url = JdbcSqliteDriver.IN_MEMORY,
                properties = properties(),
                schema = schema.synchronous(),
            )
        }.onFailure { if (it is CancellationException) throw it }
    }
}
