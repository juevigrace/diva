package io.github.juevigrace.diva.database.test

import app.cash.sqldelight.EnumColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import com.diva.database.DivaDB
import io.github.juevigrace.diva.core.DivaResult
import io.github.juevigrace.diva.core.isSuccess
import io.github.juevigrace.diva.database.DivaDatabase
import io.github.juevigrace.diva.database.driver.DriverProvider
import io.github.juevigrace.diva.database.driver.Schema
import io.github.juevigrace.diva.database.driver.configuration.DriverConf
import io.github.juevigrace.diva.database.driver.configuration.JvmConf
import io.github.juevigrace.diva.database.driver.factory.JvmDriverProviderFactory
import kotlinx.coroutines.test.runTest
import migrations.Diva_chat
import migrations.Diva_chat_participant
import migrations.Diva_collection
import migrations.Diva_media
import migrations.Diva_message
import migrations.Diva_permissions
import migrations.Diva_playlist_suggestions
import migrations.Diva_post
import migrations.Diva_session
import migrations.Diva_user
import migrations.Diva_user_preferences
import kotlin.test.Test

class DivaDBTest {
    companion object {
        private val provider: DriverProvider = JvmDriverProviderFactory(
            JvmConf(
                DriverConf.PostgresqlDriverConf(
                    host = "localhost",
                    port = 5434,
                    username = "postgres",
                    password = "postgres",
                    database = "pg",
                    schema = "public",
                )
            )
        ).create()
        private val db: DivaDatabase<DivaDB> by lazy {
            val result = provider.createDriver(Schema.Sync(DivaDB.Schema))
            assert(result.isSuccess()) {
                "INITIALIZATION ERROR: ${(result as DivaResult.Failure).err}"
            }

            val success: SqlDriver = (result as DivaResult.Success<SqlDriver>).value
            DivaDatabase(
                success,
                DivaDB(
                    driver = success,
                    diva_chatAdapter = Diva_chat.Adapter(
                        chat_typeAdapter = EnumColumnAdapter(),
                    ),
                    diva_chat_participantAdapter = Diva_chat_participant.Adapter(
                        roleAdapter = EnumColumnAdapter(),
                    ),
                    diva_collectionAdapter = Diva_collection.Adapter(
                        collection_typeAdapter = EnumColumnAdapter(),
                        visibilityAdapter = EnumColumnAdapter(),
                    ),
                    diva_mediaAdapter = Diva_media.Adapter(
                        media_typeAdapter = EnumColumnAdapter(),
                        visibilityAdapter = EnumColumnAdapter(),
                    ),
                    diva_messageAdapter = Diva_message.Adapter(
                        message_typeAdapter = EnumColumnAdapter(),
                    ),
                    diva_permissionsAdapter = Diva_permissions.Adapter(
                        role_levelAdapter = EnumColumnAdapter(),
                    ),
                    diva_playlist_suggestionsAdapter = Diva_playlist_suggestions.Adapter(
                        statusAdapter = EnumColumnAdapter(),
                    ),
                    diva_postAdapter = Diva_post.Adapter(
                        visibilityAdapter = EnumColumnAdapter(),
                    ),
                    diva_sessionAdapter = Diva_session.Adapter(
                        statusAdapter = EnumColumnAdapter(),
                    ),
                    diva_userAdapter = Diva_user.Adapter(
                        roleAdapter = EnumColumnAdapter(),
                    ),
                    diva_user_preferencesAdapter = Diva_user_preferences.Adapter(
                        themeAdapter = EnumColumnAdapter(),
                    )
                )
            )
        }
    }

    @Test
    fun `test check health`() = runTest {
        val check = db.checkHealth()
        assert(check.isSuccess()) {
            "CHECK HEALTH ERROR: ${(check as DivaResult.Failure).err}"
        }
    }
}
