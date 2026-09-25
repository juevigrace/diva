plugins {
    id("divabuild.diva-app")
    alias(libs.plugins.sqldelight)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.diva.lib.core)

            implementation(libs.diva.database.sqlite)
            implementation(libs.diva.lib.database)

            implementation(projects.core)

            implementation(projects.features.collection.collectionDatabase)
            implementation(projects.features.folder.folderDatabase)
            implementation(projects.features.library.libraryDatabase)
            implementation(projects.features.media.mediaDatabase)
            implementation(projects.features.mix.mixDatabase)
            implementation(projects.features.player.playerDatabase)
            implementation(projects.features.playlist.playlistDatabase)
            implementation(projects.features.server.serverDatabase)
        }
    }
}

sqldelight {
    databases {
        register("DivaDB") {
            packageName.set("com.diva.app.database")
            generateAsync.set(true)
            deriveSchemaFromMigrations.set(true)
            verifyMigrations.set(true)

            dependency(project(":features:collection:collection-database"))
            dependency(project(":features:folder:folder-database"))
            dependency(project(":features:library:library-database"))
            dependency(project(":features:media:media-database"))
            dependency(project(":features:mix:mix-database"))
            dependency(project(":features:player:player-database"))
            dependency(project(":features:playlist:playlist-database"))
            dependency(project(":features:server:server-database"))
        }
    }
}
