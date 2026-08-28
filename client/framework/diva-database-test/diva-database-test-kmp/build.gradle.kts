@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("divabuild.library-framework-browser-test")
    alias(libs.plugins.sqldelight)
}

kotlin {
    linuxX64 {
        binaries.getTest("DEBUG").linkerOpts("-L/usr/lib", "-lsqlite3", "--allow-shlib-undefined")
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.divaCore)
            implementation(projects.divaDatabase)
            implementation(projects.divaDatabaseSqlite)
        }
    }
}

dependencies {
    "androidHostTestImplementation"(libs.sqldelight.sqlite.driver)
    "androidHostTestImplementation"(libs.sqldelight.jdbc.driver)
    "androidDeviceTestImplementation"(libs.androidx.test.core)
}

sqldelight {
    databases {
        create("SqliteDB") {
            packageName.set("io.github.juevigrace.diva.database")
            schemaOutputDirectory.set(file("src/commonMain/sqldelight/databases"))
            generateAsync.set(true)
            deriveSchemaFromMigrations.set(true)
        }
    }
}
