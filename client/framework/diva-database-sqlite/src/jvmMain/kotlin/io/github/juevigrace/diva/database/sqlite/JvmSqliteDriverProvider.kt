package io.github.juevigrace.diva.database.sqlite

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import io.github.juevigrace.diva.database.driver.DriverProvider
import io.github.juevigrace.diva.database.sqlite.config.SqliteConf

class JvmSqliteDriverProvider(
    override val conf: SqliteConf,
) : DriverProvider<SqliteConf> {
    override fun createSyncDriver(schema: SqlSchema<QueryResult.Value<Unit>>): Result<SqlDriver> {
        return runCatching {
            JdbcSqliteDriver(
                url = url(),
                schema = schema,
            )
        }
    }

    override suspend fun createAsyncDriver(schema: SqlSchema<QueryResult.AsyncValue<Unit>>): Result<SqlDriver> {
        return runCatching {
            JdbcSqliteDriver(
                url = url(),
                schema = schema.synchronous(),
            )
        }
    }

    private fun url(): String {
        return if (conf.inMemory) {
            JdbcSqliteDriver.IN_MEMORY
        } else {
            "jdbc:sqlite:${conf.name}"
        }
    }
}
