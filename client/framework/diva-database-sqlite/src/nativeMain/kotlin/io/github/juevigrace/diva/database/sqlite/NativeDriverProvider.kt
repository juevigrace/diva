package io.github.juevigrace.diva.database.sqlite

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import io.github.juevigrace.diva.database.driver.DriverProvider
import io.github.juevigrace.diva.database.sqlite.config.SqliteConf

class NativeDriverProvider(
    override val conf: SqliteConf,
) : DriverProvider<SqliteConf> {
    override fun createSyncDriver(schema: SqlSchema<QueryResult.Value<Unit>>): Result<SqlDriver> {
        return runCatching {
            NativeSqliteDriver(
                schema = schema,
                name = conf.name,
                onConfiguration = { configuration ->
                    if (conf.inMemory) configuration.copy(inMemory = true) else configuration
                },
            )
        }
    }

    override suspend fun createAsyncDriver(schema: SqlSchema<QueryResult.AsyncValue<Unit>>): Result<SqlDriver> {
        return runCatching {
            NativeSqliteDriver(
                schema = schema.synchronous(),
                name = conf.name,
                onConfiguration = { configuration ->
                    if (conf.inMemory) configuration.copy(inMemory = true) else configuration
                },
            )
        }
    }
}
