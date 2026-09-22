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

            implementation(projects.features.collectionDatabase)
            implementation(projects.features.folderDatabase)
            implementation(projects.features.libraryDatabase)
            implementation(projects.features.mediaDatabase)
            implementation(projects.features.mixDatabase)
            implementation(projects.features.playerDatabase)
            implementation(projects.features.playlistDatabase)
            implementation(projects.features.serverDatabase)
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

            dependency(project(":features:collection-database"))
            dependency(project(":features:folder-database"))
            dependency(project(":features:library-database"))
            dependency(project(":features:media-database"))
            dependency(project(":features:mix-database"))
            dependency(project(":features:player-database"))
            dependency(project(":features:playlist-database"))
            dependency(project(":features:server-database"))
        }
    }
}
