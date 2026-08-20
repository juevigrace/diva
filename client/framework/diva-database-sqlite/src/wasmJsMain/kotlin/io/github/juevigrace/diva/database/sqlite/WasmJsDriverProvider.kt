package io.github.juevigrace.diva.database.sqlite

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.worker.createDefaultWebWorkerDriver
import io.github.juevigrace.diva.database.driver.DriverProvider
import io.github.juevigrace.diva.database.sqlite.config.SqliteConf

class WasmJsDriverProvider(
    override val conf: SqliteConf,
) : DriverProvider<SqliteConf> {
    override fun createSyncDriver(schema: SqlSchema<QueryResult.Value<Unit>>): Result<SqlDriver> {
        return Result.failure(
            UnsupportedOperationException("The Web Worker driver is async-only"),
        )
    }

    override suspend fun createAsyncDriver(schema: SqlSchema<QueryResult.AsyncValue<Unit>>): Result<SqlDriver> {
        return runCatching {
            createDefaultWebWorkerDriver().also { driver ->
                schema.create(driver).await()
            }
        }
    }
}
