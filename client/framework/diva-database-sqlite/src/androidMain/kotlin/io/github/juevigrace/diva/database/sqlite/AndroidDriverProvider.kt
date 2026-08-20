package io.github.juevigrace.diva.database.sqlite

import android.content.Context
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import io.github.juevigrace.diva.database.driver.DriverProvider
import io.github.juevigrace.diva.database.sqlite.config.SqliteConf

class AndroidDriverProvider(
    private val context: Context,
    override val conf: SqliteConf,
) : DriverProvider<SqliteConf> {
    override fun createSyncDriver(schema: SqlSchema<QueryResult.Value<Unit>>): Result<SqlDriver> {
        return runCatching {
            AndroidSqliteDriver(
                schema = schema,
                context = context,
                name = if (conf.inMemory) null else conf.name,
            )
        }
    }

    override suspend fun createAsyncDriver(schema: SqlSchema<QueryResult.AsyncValue<Unit>>): Result<SqlDriver> {
        return runCatching {
            AndroidSqliteDriver(
                schema = schema.synchronous(),
                context = context,
                name = if (conf.inMemory) null else conf.name,
            )
        }
    }
}
