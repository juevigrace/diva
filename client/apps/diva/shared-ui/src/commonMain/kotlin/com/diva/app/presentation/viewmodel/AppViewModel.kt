package com.diva.app.presentation.viewmodel

import com.diva.app.presentation.state.AppState
import com.diva.app.settings.domain.SettingsRepository
import io.github.juevigrace.diva.core.getOrNull
import io.github.juevigrace.diva.lib.core.models.Role
import io.github.juevigrace.diva.lib.device.models.Device
import io.github.juevigrace.diva.lib.devices.domain.DevicesRepository
import io.github.juevigrace.diva.lib.session.domain.SessionRepository
import io.github.juevigrace.diva.lib.session.models.Session
import io.github.juevigrace.diva.lib.session.models.SessionData
import io.github.juevigrace.diva.lib.session.models.SessionStatus
import io.github.juevigrace.diva.lib.session.models.SessionType
import io.github.juevigrace.diva.lib.settings.models.AppSettings
import io.github.juevigrace.diva.lib.user.domain.UserRepository
import io.github.juevigrace.diva.lib.user.models.User
import io.github.juevigrace.diva.network.client.DivaClient
import io.github.juevigrace.diva.ui.viewmodel.DivaViewModel
import io.ktor.client.plugins.DefaultRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.time.Clock

/**
 * Root singleton, created eagerly at startup. Seeds the first-run rows and then pushes the
 * resulting host/port/protocol into the [DivaClient].
 */
class AppViewModel(
    private val defaults: AppSettings,
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository,
    private val settingsRepository: SettingsRepository,
    private val devicesRepository: DevicesRepository,
    private val client: DivaClient,
) : DivaViewModel() {
    val state: StateFlow<AppState>
        field = MutableStateFlow(AppState(settings = defaults))

    private var appliedBaseUrl: String? = null

    init {
        runBlocking {
            seed()
            val settings = sessionRepository.getCurrent().mapCatching { session ->
                settingsRepository.get(session.userId).getOrThrow()
            }
            publish(settings)
        }
        scope.launch { observeSettings() }
    }

    private suspend fun seed() {
        seedUser()
        seedDevice()
        seedSession()
        seedSettings()
    }

    private suspend fun seedUser() {
        if (userRepository.getUser(LOCAL_USER_ID).first().getOrNull()?.isSome == true) return
        userRepository.save(User(id = LOCAL_USER_ID, username = LOCAL_USERNAME, role = Role.USER))
    }

    private suspend fun seedDevice() {
        if (devicesRepository.getDevice(LOCAL_DEVICE_ID).first().getOrNull()?.isSome == true) return
        val now = Clock.System.now().toEpochMilliseconds()
        // TODO: get actual device name
        val deviceName = LOCAL_USERNAME
        devicesRepository.save(
            Device(id = LOCAL_DEVICE_ID, name = deviceName, createdAt = now, updatedAt = now),
        )
    }

    private suspend fun seedSession() {
        if (sessionRepository.getCurrentFlow().first().getOrNull()?.isSome == true) return
        val now = Clock.System.now().toEpochMilliseconds()
        sessionRepository.save(
            Session(
                id = LOCAL_SESSION_ID,
                userId = LOCAL_USER_ID,
                accessToken = "",
                refreshToken = "",
                type = SessionType.LOCAL,
                status = SessionStatus.ACTIVE,
                data = SessionData(device = LOCAL_DEVICE_ID),
                accessExpiresAt = 0L,
                refreshExpiresAt = 0L,
                createdAt = now,
                updatedAt = now,
            ),
        )
        sessionRepository.markCurrent(LOCAL_SESSION_ID)
    }

    private suspend fun seedSettings() {
        if (settingsRepository.get(LOCAL_USER_ID).isSuccess) return
        settingsRepository.upsert(LOCAL_USER_ID, defaults)
    }

    private suspend fun observeSettings() {
        sessionRepository.getCurrent().fold(
            onSuccess = { session ->
                settingsRepository.observe(session.userId).collect { result ->
                    publish(result)
                }
            },
            onFailure = { },
        )
    }

    private fun publish(settings: Result<AppSettings>) {
        settings.onSuccess(::applyNetworkConfig)
    }

    private fun applyNetworkConfig(settings: AppSettings) {
        val baseUrl = buildBaseUrl(settings)
        if (baseUrl == appliedBaseUrl) return
        appliedBaseUrl = baseUrl
        client.config {
            installOrReplace(DefaultRequest) { url(baseUrl) }
        }
    }

    private fun buildBaseUrl(settings: AppSettings): String {
        val protocol = settings.protocol.ifBlank { "http" }
        val host = settings.host.ifBlank { "localhost" }
        val port = settings.port.getOrNull()
        val authority = if (port != null) "$host:$port" else host
        return "$protocol://$authority/"
    }

    private companion object {
        const val LOCAL_USER_ID = "local"
        const val LOCAL_USERNAME = "Local"
        const val LOCAL_DEVICE_ID = "local"
        const val LOCAL_SESSION_ID = "local"
    }
}
