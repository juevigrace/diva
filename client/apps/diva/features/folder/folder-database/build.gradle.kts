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
        }
    }
}

sqldelight {
    databases {
        register("DivaDB") {
            packageName.set("com.diva.app.database.folder")
            schemaOutputDirectory.set(file("src/commonMain/sqldelight/databases"))
            generateAsync.set(true)
            deriveSchemaFromMigrations.set(true)
            verifyMigrations.set(true)

            dependency(project(":features:media:media-database"))
        }
    }
}
