plugins {
    id("divabuild.library-package")
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.sqldelight)
}
dependencies {
    implementation(projects.divaCore)
    implementation(projects.divaDatabase)

    implementation(libs.postgresql)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.koin.test)
}

sqldelight {
    databases {
        create("DB") {
            packageName.set("io.github.juevigrace.diva.database")
            schemaOutputDirectory.set(file("src/main/sqldelight/databases"))
            generateAsync.set(false)
        }
    }
}
