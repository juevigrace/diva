package com.diva.app.database

import app.cash.sqldelight.EnumColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import migrations.Diva_collection
import migrations.Diva_media
import migrations.Diva_player_setting
import migrations.Diva_playlist_suggestions

fun appDivaDBMapper(driver: SqlDriver): DivaDB {
    return DivaDB(
        driver = driver,
        diva_mediaAdapter = Diva_media.Adapter(EnumColumnAdapter(), EnumColumnAdapter()),
        diva_collectionAdapter = Diva_collection.Adapter(EnumColumnAdapter(), EnumColumnAdapter()),
        diva_playlist_suggestionsAdapter = Diva_playlist_suggestions.Adapter(EnumColumnAdapter()),
        diva_player_settingAdapter = Diva_player_setting.Adapter(EnumColumnAdapter()),
    )
}
