package com.diva.server.database

import com.diva.database.user.UserStorage
import com.diva.models.permission.Permission
import com.diva.models.permissions.Permissions
import com.diva.models.roles.Role
import com.diva.models.user.User
import com.diva.util.Encryption
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.isPresent
import io.github.juevigrace.diva.core.isSuccess
import io.github.juevigrace.diva.core.onFailure
import io.github.juevigrace.diva.core.onSome
import io.github.juevigrace.diva.core.onSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

// TODO: add permissions storage
class Seed(
    private val userStorage: UserStorage,
) {
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var rootUser: Option<User> = Option.None

    @OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
    fun addRootUser(username: String, email: String, password: String): Seed {
        val passwordHash: String = Encryption.hashPassword(password)
        rootUser = Option.of(
            User(
                id = Uuid.random(),
                email = email,
                username = username,
                passwordHash = Option.of(passwordHash),
                permissions = listOf()
            )
        )
        return this
    }

    @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
    fun seed() {
        scope.launch {
            launch {
                rootUser.onSome { user ->
                    userStorage.getByUsername(user.username).onSuccess { u ->
                        if (u.isPresent()) {
                            return@launch
                        }
                    }
                    userStorage.insert(user).onFailure { err ->
                        println("Error inserting root user: $err")
                    }
                }
            }
            launch {
            // todo: Insert permissions
            }
        }
    }

    companion object {
        @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
        private val CREATE_USER_WRITE = Permission(
            id = Uuid.random(),
            name = Permissions.Write.CreateUser.name,
            description = "Create new users",
            roleLevel = Role.ADMIN,
            createdAt = Clock.System.now(),
            updatedAt = Clock.System.now()
        )
    }
}
