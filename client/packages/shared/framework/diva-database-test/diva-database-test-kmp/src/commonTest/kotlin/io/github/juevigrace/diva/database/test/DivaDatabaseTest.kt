package io.github.juevigrace.diva.database.test

import app.cash.sqldelight.db.SqlDriver
import io.github.juevigrace.diva.database.DivaDatabase
import io.github.juevigrace.diva.database.SqliteDB
import io.github.juevigrace.diva.database.driver.DriverProvider
import io.github.juevigrace.diva.database.sqlite.config.SqliteConf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

expect val provider: DriverProvider<SqliteConf>

class DivaDatabaseTest {

    companion object {
        private suspend fun createDatabase(): DivaDatabase<SqliteDB> {
            val driver: SqlDriver = provider.createAsyncDriver(SqliteDB.Schema).getOrThrow()
            return DivaDatabase.createAsync(provider, SqliteDB.Schema, db = { SqliteDB(driver) }).getOrThrow()
        }
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

    @Test
    fun test_foreignKeysRejectOrphanInsert() = runTest {
        val db = createDatabase()
        db.use {
            val result = runCatching { postsQueries.insertPost(999_999L, "orphan post") }
            assertTrue(result.isFailure, "Expected a foreign key violation when inserting a post with no matching user")
        }
        db.close()
    }

    @Test
    fun test_foreignKeysCascadeOnDelete() = runTest {
        val db = createDatabase()
        db.use {
            usersQueries.insertUser("Alice", "alice@test.com", 0)
            val userId = usersQueries.selectAll().executeAsList()[0].id
            postsQueries.insertPost(userId, "first post")
            assertEquals(1, postsQueries.selectAllPosts().executeAsList().size)
            usersQueries.deleteById(userId)
            assertTrue(postsQueries.selectAllPosts().executeAsList().isEmpty())
        }
        db.close()
    }
}
