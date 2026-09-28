plugins {
    id("divabuild.diva-app")
    alias(libs.plugins.sqldelight)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.diva.database.sqlite)

            implementation(projects.core)

            implementation(projects.features.server.serverModels)
        }
    }
}

sqldelight {
    databases {
        register("DivaDB") {
            packageName.set("com.diva.app.server.database")
            schemaOutputDirectory.set(file("src/commonMain/sqldelight/databases"))
            generateAsync.set(true)
            deriveSchemaFromMigrations.set(true)
            verifyMigrations.set(true)
        }
    }
}
