package io.github.juevigrace.diva.database.sqlite

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import co.touchlab.sqliter.DatabaseConfiguration
import io.github.juevigrace.diva.database.driver.DriverProvider
import io.github.juevigrace.diva.database.exception.DatabaseExceptionTransformer
import io.github.juevigrace.diva.database.sqlite.config.SqliteConf
import io.github.juevigrace.diva.database.sqlite.exception.SqliteExceptionTransformer

class NativeDriverProvider(
    override val conf: SqliteConf,
) : DriverProvider<SqliteConf> {
    override val transformer: DatabaseExceptionTransformer = SqliteExceptionTransformer

    override fun createSyncDriver(schema: SqlSchema<QueryResult.Value<Unit>>): Result<SqlDriver> {
        return runCatching {
            NativeSqliteDriver(
                schema = schema,
                name = if (conf.inMemory) "" else conf.name,
                onConfiguration = ::onConfiguration,
            )
        }
    }

    override suspend fun createAsyncDriver(schema: SqlSchema<QueryResult.AsyncValue<Unit>>): Result<SqlDriver> {
        return runCatching {
            NativeSqliteDriver(
                schema = schema.synchronous(),
                name = if (conf.inMemory) "" else conf.name,
                onConfiguration = ::onConfiguration,
            )
        }
    }

    private fun onConfiguration(configuration: DatabaseConfiguration): DatabaseConfiguration {
        return configuration
            .let { if (conf.inMemory) it.copy(name = null, inMemory = true) else it }
            .let {
                if (conf.foreignKeys) {
                    it.copy(extendedConfig = DatabaseConfiguration.Extended(foreignKeyConstraints = true))
                } else {
                    it
                }
            }
    }
}
