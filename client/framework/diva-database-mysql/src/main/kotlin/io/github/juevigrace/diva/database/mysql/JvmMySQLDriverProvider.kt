package io.github.juevigrace.diva.database.mysql

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.jdbc.asJdbcDriver
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.juevigrace.diva.database.driver.DriverProvider
import io.github.juevigrace.diva.database.mysql.config.MysqlConf

class JvmMySQLDriverProvider(
    override val conf: MysqlConf,
) : DriverProvider<MysqlConf> {
    override fun createSyncDriver(schema: SqlSchema<QueryResult.Value<Unit>>): Result<SqlDriver> {
        return runCatching {
            val driver = createDataSource().asJdbcDriver()
            schema.create(driver)
            driver
        }
    }

    override suspend fun createAsyncDriver(schema: SqlSchema<QueryResult.AsyncValue<Unit>>): Result<SqlDriver> {
        return runCatching {
            val driver = createDataSource().asJdbcDriver()
            schema.create(driver).await()
            driver
        }
    }

    private fun createDataSource(): HikariDataSource {
        val config = HikariConfig().apply {
            jdbcUrl = "jdbc:mysql://${conf.host}:${conf.port}/${conf.database}"
            username = conf.username
            password = conf.password
            conf.properties.forEach { (key, value) -> addDataSourceProperty(key, value) }
        }
        return HikariDataSource(config)
    }
}
