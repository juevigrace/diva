package io.github.juevigrace.diva.database.test

import app.cash.sqldelight.db.SqlDriver
import io.github.juevigrace.diva.database.DB
import io.github.juevigrace.diva.database.DivaDB
import io.github.juevigrace.diva.database.DivaDatabase
import io.github.juevigrace.diva.database.driver.DriverProvider
import io.github.juevigrace.diva.database.driver.Schema
import io.github.juevigrace.diva.database.driver.configuration.DriverConf
import io.github.juevigrace.diva.database.driver.configuration.JvmConf
import io.github.juevigrace.diva.database.driver.factory.JvmDriverProviderFactory
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class DivaDBTest {
    companion object {
        private val provider: DriverProvider = JvmDriverProviderFactory(
            JvmConf(
                DriverConf.SqliteDriverConf(
                    name = ":memory:",
                )
            )
        ).create()
        private val db: DivaDatabase<DB> by lazy { createDatabase() }

        val divaDB = DivaDB(db)

        fun createDatabase(): DivaDatabase<DB> {
            val result = provider.createDriver(Schema.Sync(DB.Schema))
            assert(result.isSuccess) {
                "INITIALIZATION ERROR: ${result.getOrNull()}"
            }

            val success: SqlDriver = result.getOrThrow()
            return DivaDatabase(success, DB(success))
        }
    }

    @Test
    fun `test check health`() = runTest {
        val check = db.checkHealth()
        assert(check.isSuccess) {
            "CHECK HEALTH ERROR: ${check.getOrNull()}"
        }
    }

    @Test
    fun `test count users query`() = runTest {
        val count = divaDB.count()
        assert(count.isSuccess) {
            "COUNT ERROR: ${count.getOrNull()}"
        }
        println(count.getOrThrow())
    }

    @Test
    fun `test close`() = runTest {
        val close = createDatabase().close()
        assert(close.isSuccess)
    }
}
