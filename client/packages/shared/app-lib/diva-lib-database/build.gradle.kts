plugins {
    id("divabuild.library-app")
    id("divabuild.serialization")
    alias(libs.plugins.sqldelight)
}

sqldelight {
    databases {
        create("DivaDB") {
            packageName.set("io.github.juevigrace.diva.lib.database")
            schemaOutputDirectory.set(file("src/commonMain/sqldelight/databases"))
            generateAsync.set(true)
            deriveSchemaFromMigrations.set(true)
            verifyMigrations.set(true)
        }
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.diva.database)
            api(libs.diva.database.sqlite)

            api(projects.divaLibModelsCore)
        }
    }
}
