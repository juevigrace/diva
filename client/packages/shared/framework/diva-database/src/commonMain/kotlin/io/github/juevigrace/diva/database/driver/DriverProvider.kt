package io.github.juevigrace.diva.database.driver

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import io.github.juevigrace.diva.database.config.DriverConf
import io.github.juevigrace.diva.database.exception.DatabaseExceptionTransformer

interface DriverProvider<C : DriverConf> {
    val conf: C
    val transformer: DatabaseExceptionTransformer

    fun createSyncDriver(schema: SqlSchema<QueryResult.Value<Unit>>): Result<SqlDriver>

    suspend fun createAsyncDriver(schema: SqlSchema<QueryResult.AsyncValue<Unit>>): Result<SqlDriver>
}
