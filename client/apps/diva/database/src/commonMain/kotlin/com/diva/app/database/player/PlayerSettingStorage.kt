package com.diva.app.database.player

import com.diva.app.models.player.PlayerSetting
import io.github.juevigrace.diva.core.Option
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

interface PlayerSettingStorage {
    suspend fun getByUser(userId: Uuid): Result<Option<PlayerSetting>>

    fun getByUserFlow(userId: Uuid): Flow<Result<Option<PlayerSetting>>>

    suspend fun upsert(item: PlayerSetting): Result<Unit>

    suspend fun deleteByUser(userId: Uuid): Result<Unit>

    suspend fun deleteAll(): Result<Unit>
}
