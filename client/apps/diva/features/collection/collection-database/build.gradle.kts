plugins {
    id("divabuild.diva-app")
    alias(libs.plugins.sqldelight)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.diva.database.sqlite)

            implementation(projects.core)

            implementation(projects.features.media.mediaDatabase)

            implementation(projects.features.collection.collectionModels)
            implementation(projects.features.media.mediaModels)
        }
    }
}

sqldelight {
    databases {
        register("DivaDB") {
            packageName.set("com.diva.app.collection.database")
            schemaOutputDirectory.set(file("src/commonMain/sqldelight/databases"))
            generateAsync.set(true)
            deriveSchemaFromMigrations.set(true)
            verifyMigrations.set(true)

            dependency(project(":features:media:media-database"))
        }
    }
}
