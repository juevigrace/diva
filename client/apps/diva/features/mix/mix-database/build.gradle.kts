plugins {
    id("divabuild.diva-app")
    alias(libs.plugins.sqldelight)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core)

            implementation(projects.features.collection.collectionDatabase)
            implementation(projects.features.collection.collectionModels)

            implementation(projects.features.media.mediaDatabase)

            api(libs.diva.database.sqlite)
        }
    }
}

sqldelight {
    databases {
        register("DivaDB") {
            packageName.set("com.diva.app.mix.database")
            schemaOutputDirectory.set(file("src/commonMain/sqldelight/databases"))
            generateAsync.set(true)
            deriveSchemaFromMigrations.set(true)
            verifyMigrations.set(true)

            dependency(project(":features:collection:collection-database"))
            dependency(project(":features:media:media-database"))
        }
    }
}
