package com.diva.app.presentation.viewmodel

import com.diva.app.presentation.state.AppState
import com.diva.app.settings.domain.SettingsRepository
import com.diva.app.settings.models.AppSettings
import io.github.juevigrace.diva.core.getOrNull
import io.github.juevigrace.diva.lib.core.models.Role
import io.github.juevigrace.diva.lib.device.models.Device
import io.github.juevigrace.diva.lib.devices.domain.DevicesRepository
import io.github.juevigrace.diva.lib.session.domain.SessionRepository
import io.github.juevigrace.diva.lib.session.models.Session
import io.github.juevigrace.diva.lib.session.models.SessionData
import io.github.juevigrace.diva.lib.session.models.SessionStatus
import io.github.juevigrace.diva.lib.session.models.SessionType
import io.github.juevigrace.diva.lib.settings.models.Settings
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
            publish(settingsRepository.get())
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
        if (userRepository.observeCurrent().first().getOrNull() != null) return
        userRepository.upsert(User(id = LOCAL_USER_ID, username = LOCAL_USERNAME, role = Role.USER))
    }

    private suspend fun seedDevice() {
        if (devicesRepository.observe(LOCAL_DEVICE_ID).first().getOrNull() != null) return
        val now = Clock.System.now().toEpochMilliseconds()
        // TODO: get actual device name
        val deviceName = LOCAL_USERNAME
        devicesRepository.upsert(
            Device(id = LOCAL_DEVICE_ID, name = deviceName, createdAt = now, updatedAt = now),
        )
    }

    private suspend fun seedSession() {
        if (sessionRepository.observe().first().getOrNull() != null) return
        val now = Clock.System.now().toEpochMilliseconds()
        sessionRepository.upsert(
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
        if (settingsRepository.get().isSuccess) return
        settingsRepository.upsert(defaults)
    }

    private suspend fun observeSettings() {
        settingsRepository.observe().collect { result ->
            publish(result)
        }
    }

    private fun publish(settings: Result<Settings>) {
        settings.onSuccess(::applyNetworkConfig)
    }

    private fun applyNetworkConfig(settings: Settings) {
        val baseUrl = buildBaseUrl(settings)
        if (baseUrl == appliedBaseUrl) return
        appliedBaseUrl = baseUrl
        client.config {
            installOrReplace(DefaultRequest) { url(baseUrl) }
        }
    }

    private fun buildBaseUrl(settings: Settings): String {
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
