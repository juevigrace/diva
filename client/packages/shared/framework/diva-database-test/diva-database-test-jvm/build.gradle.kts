plugins {
    id("divabuild.library-framework-jvm")
    alias(libs.plugins.sqldelight)
}

dependencies {
    implementation(projects.divaCore)
    implementation(projects.divaDatabase)
    implementation(projects.divaDatabaseMysql)
    implementation(projects.divaDatabasePostgres)

    testImplementation(libs.kotlin.test.junit5)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.testcontainers.postgresql)
    testImplementation(libs.testcontainers.mysql)
    testImplementation(libs.testcontainers.jdbc)
    testImplementation(libs.testcontainers.junit.jupiter)
}

tasks.test {
    useJUnitPlatform()
}

sqldelight {
    databases {
        create("MysqlDB") {
            packageName.set("io.github.juevigrace.diva.database.mysql")
            schemaOutputDirectory.set(file("src/main/sqldelight/databases"))
            srcDirs(file("src/main/sqldelight/MysqlDB"))
            dialect(libs.sqldelight.mysql.dialect)
            generateAsync.set(true)
            deriveSchemaFromMigrations.set(true)
        }
        create("PostgresDB") {
            packageName.set("io.github.juevigrace.diva.database.postgres")
            schemaOutputDirectory.set(file("src/main/sqldelight/databases"))
            srcDirs(file("src/main/sqldelight/PostgresDB"))
            dialect(libs.sqldelight.postgres.dialect)
            generateAsync.set(true)
            deriveSchemaFromMigrations.set(true)
        }
    }
}
