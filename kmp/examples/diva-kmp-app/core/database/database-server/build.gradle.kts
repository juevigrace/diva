plugins {
    id("divabuild.library-server")
    alias(libs.plugins.sqldelight)
}

dependencies {
    api(projects.core.database.databaseShared)
    implementation(projects.core.modelsServer)

    implementation(libs.postgresql)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.koin.test)
}

sqldelight {
    databases {
        create("DivaDB") {
            packageName.set("com.diva.database")
            schemaOutputDirectory.set(file("src/main/sqldelight/databases"))
            dialect(libs.sqldelight.postgres.dialect)
            generateAsync.set(false)
            deriveSchemaFromMigrations.set(true)
            verifyMigrations.set(true)
        }
    }
}
