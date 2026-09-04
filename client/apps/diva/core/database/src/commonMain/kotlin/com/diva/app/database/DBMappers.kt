package com.diva.app.database

import app.cash.sqldelight.EnumColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import io.github.juevigrace.diva.lib.database.DivaSharedDB
import migrations.Diva_action
import migrations.Diva_permissions
import migrations.Diva_session
import migrations.Diva_user
import migrations.Diva_user_preferences
import migrations.Diva_user_state

fun sharedDBMapper(driver: SqlDriver): DivaSharedDB {
    return DivaSharedDB(
        driver = driver,
        diva_actionAdapter = Diva_action.Adapter(EnumColumnAdapter()),
        diva_permissionsAdapter = Diva_permissions.Adapter(EnumColumnAdapter()),
        diva_sessionAdapter = Diva_session.Adapter(EnumColumnAdapter(), EnumColumnAdapter()),
        diva_userAdapter = Diva_user.Adapter(EnumColumnAdapter()),
        diva_user_preferencesAdapter = Diva_user_preferences.Adapter(EnumColumnAdapter()),
        diva_user_stateAdapter = Diva_user_state.Adapter(EnumColumnAdapter()),
    )
}

fun appDivaDBMapper(driver: SqlDriver): DivaDB {
    return DivaDB(driver)
}
