package com.diva.server.di.database

import app.cash.sqldelight.EnumColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import com.diva.database.DivaDB
import io.github.juevigrace.diva.core.getOrThrow
import io.github.juevigrace.diva.database.DivaDatabase
import io.github.juevigrace.diva.database.driver.DriverProvider
import io.github.juevigrace.diva.database.driver.Schema
import io.github.juevigrace.diva.database.driver.configuration.DriverConf
import io.github.juevigrace.diva.database.driver.configuration.JvmConf
import io.github.juevigrace.diva.database.driver.factory.JvmDriverProviderFactory
import io.ktor.server.config.ApplicationConfig
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
import org.koin.core.module.Module
import org.koin.dsl.module

fun databaseModule(config: ApplicationConfig): Module {
    return module {
        single<SqlDriver> {
            val config = JvmConf(
                DriverConf.PostgresqlDriverConf(
                    host = config.property("database.host").getString(),
                    port = config.property("database.port").getString().toInt(),
                    username = config.property("database.user").getString(),
                    password = config.property("database.password").getString(),
                    database = config.property("database.name").getString(),
                    schema = config.property("database.schema").getString()
                )
            )
            val provider: DriverProvider = JvmDriverProviderFactory(config).create()
            // TODO: find a way to not throw
            val driver = provider.createDriver(Schema.Sync(DivaDB.Schema))
            driver.getOrThrow()
        }

        single<DivaDatabase<DivaDB>> {
            val driver: SqlDriver = get()
            DivaDatabase(
                driver = driver,
                db = DivaDB(
                    driver = driver,
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
                    ),
                ),
            )
        }
    }
}
