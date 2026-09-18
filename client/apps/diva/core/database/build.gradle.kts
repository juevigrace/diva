plugins {
    id("divabuild.library-app-shared")
    alias(libs.plugins.sqldelight)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.diva.lib.core)
            implementation(libs.diva.lib.models.core)

            implementation(libs.diva.database.sqlite)
            implementation(libs.diva.lib.database)

            implementation(projects.core.models)
        }
    }
}

sqldelight {
    databases {
        create("DivaDB") {
            packageName.set("com.diva.app.database")
            schemaOutputDirectory.set(file("src/commonMain/sqldelight/databases"))
            generateAsync.set(true)
            deriveSchemaFromMigrations.set(true)
            verifyMigrations.set(true)
        }
    }
}
