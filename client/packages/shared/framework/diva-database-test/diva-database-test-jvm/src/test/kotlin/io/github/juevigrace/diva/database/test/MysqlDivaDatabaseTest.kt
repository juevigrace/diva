package io.github.juevigrace.diva.database.test

import app.cash.sqldelight.db.SqlDriver
import io.github.juevigrace.diva.database.DivaDatabase
import io.github.juevigrace.diva.database.mysql.MysqlDB
import io.github.juevigrace.diva.database.mysql.JvmMySQLDriverProvider
import io.github.juevigrace.diva.database.mysql.config.MysqlConf
import kotlinx.coroutines.test.runTest
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Testcontainers
class MysqlDivaDatabaseTest {

    @Container
    private var mysql = MySQLContainer("mysql:8.0")

    private suspend fun createDatabase(): DivaDatabase<MysqlDB> {
        val provider = JvmMySQLDriverProvider(
            MysqlConf(
                host = mysql.host,
                port = mysql.firstMappedPort,
                database = mysql.databaseName,
                username = mysql.username,
                password = mysql.password,
            )
        )
        val driver: SqlDriver = provider.createAsyncDriver(MysqlDB.Schema).getOrThrow()
        return DivaDatabase.createAsync(provider, MysqlDB.Schema, db = { MysqlDB(driver) }).getOrThrow()
    }

    @Test
    fun test_insert_and_selectAll() = runTest {
        val db = createDatabase()
        db.use {
            usersQueries.insertUser("Alice", "alice@test.com", 0)
            val users = usersQueries.selectAll().executeAsList()
            assertEquals(1, users.size)
            assertEquals("Alice", users[0].name)
            assertEquals("alice@test.com", users[0].email)
        }
        db.close()
    }

    @Test
    fun test_selectById() = runTest {
        val db = createDatabase()
        db.use {
            usersQueries.insertUser("Bob", "bob@test.com", 0)
            val users = usersQueries.selectAll().executeAsList()
            val id = users[0].id
            val user = usersQueries.selectById(id).executeAsOneOrNull()
            assertEquals("Bob", user?.name)
            assertEquals("bob@test.com", user?.email)
        }
        db.close()
    }

    @Test
    fun test_selectByEmail() = runTest {
        val db = createDatabase()
        db.use {
            usersQueries.insertUser("Charlie", "charlie@test.com", 0)
            val user = usersQueries.selectByEmail("charlie@test.com").executeAsOneOrNull()
            assertEquals("Charlie", user?.name)
        }
        db.close()
    }

    @Test
    fun test_deleteById() = runTest {
        val db = createDatabase()
        db.use {
            usersQueries.insertUser("Dave", "dave@test.com", 0)
            val users = usersQueries.selectAll().executeAsList()
            val id = users[0].id
            usersQueries.deleteById(id)
            val remaining = usersQueries.selectAll().executeAsList()
            assertTrue(remaining.isEmpty())
        }
        db.close()
    }

    @Test
    fun test_close() = runTest {
        val db = createDatabase()
        val close = db.close()
        assertTrue(close.isSuccess, "CLOSE ERROR: ${close.exceptionOrNull()}")
    }
}
