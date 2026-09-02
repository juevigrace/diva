package io.github.juevigrace.diva.database.test

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import io.github.juevigrace.diva.database.driver.DriverProvider
import io.github.juevigrace.diva.database.exception.DatabaseExceptionTransformer
import io.github.juevigrace.diva.database.sqlite.config.SqliteConf
import io.github.juevigrace.diva.database.sqlite.exception.SqliteExceptionTransformer

actual val provider: DriverProvider<SqliteConf> = object : DriverProvider<SqliteConf> {
    override val conf = SqliteConf("test", inMemory = true)
    override val transformer: DatabaseExceptionTransformer = SqliteExceptionTransformer

    override fun createSyncDriver(schema: SqlSchema<QueryResult.Value<Unit>>): Result<SqlDriver> {
        return runCatching {
            JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).also {
                schema.create(it)
            }
        }
    }

    override suspend fun createAsyncDriver(schema: SqlSchema<QueryResult.AsyncValue<Unit>>): Result<SqlDriver> {
        return runCatching {
            JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).also {
                schema.synchronous().create(it)
            }
        }
    }
}
