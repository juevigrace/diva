plugins {
    id("divabuild.diva-app")
    alias(libs.plugins.sqldelight)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.diva.database.sqlite)

            implementation(projects.core)

            implementation(projects.features.media.mediaModels)
            implementation(projects.features.collection.collectionModels)
        }
    }
}

sqldelight {
    databases {
        register("DivaDB") {
            packageName.set("com.diva.app.media.database")
            schemaOutputDirectory.set(file("src/commonMain/sqldelight/databases"))
            generateAsync.set(true)
            deriveSchemaFromMigrations.set(true)
            verifyMigrations.set(true)
        }
    }
}
