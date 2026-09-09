package com.diva.app.database.player

import com.diva.app.models.player.PlayerSetting
import io.github.juevigrace.diva.core.Option
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface PlayerSettingStorage {
    @OptIn(ExperimentalUuidApi::class)
    suspend fun getByUser(userId: Uuid): Result<Option<PlayerSetting>>

    @OptIn(ExperimentalUuidApi::class)
    fun getByUserFlow(userId: Uuid): Flow<Result<Option<PlayerSetting>>>

    suspend fun upsert(item: PlayerSetting): Result<Unit>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun deleteByUser(userId: Uuid): Result<Unit>

    suspend fun deleteAll(): Result<Unit>
}
