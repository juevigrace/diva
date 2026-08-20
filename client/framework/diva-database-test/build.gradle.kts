@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("divabuild.library-framework")
    id("divabuild.kmp-test")
    alias(libs.plugins.sqldelight)
}

kotlin {
    js {
        browser()
    }
    wasmJs {
        browser()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":diva-core"))
            implementation(project(":diva-database"))
            implementation(project(":diva-database-sqlite"))
        }
        jvmMain.dependencies {
            implementation(project(":diva-database-mysql"))
            implementation(project(":diva-database-postgres"))
        }
    }
}

sqldelight {
    databases {
        create("SqliteDB") {
            packageName.set("io.github.juevigrace.diva.database")
            schemaOutputDirectory.set(file("src/commonMain/sqldelight/databases"))
            srcDirs(file("src/commonMain/sqldelight/SqliteDB"))
            generateAsync.set(true)
        }
        create("MysqlDB") {
            packageName.set("io.github.juevigrace.diva.database.mysql")
            schemaOutputDirectory.set(file("src/jvmMain/sqldelight/databases"))
            srcDirs(file("src/jvmMain/sqldelight/MysqlDB"))
            dialect("app.cash.sqldelight:mysql-dialect:2.3.2")
            generateAsync.set(true)
        }
        create("PostgresDB") {
            packageName.set("io.github.juevigrace.diva.database.postgres")
            schemaOutputDirectory.set(file("src/jvmMain/sqldelight/databases"))
            srcDirs(file("src/jvmMain/sqldelight/PostgresDB"))
            dialect("app.cash.sqldelight:postgresql-dialect:2.3.2")
            generateAsync.set(true)
        }
    }
}