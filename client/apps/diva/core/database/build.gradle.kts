@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("divabuild.library-app")
    alias(libs.plugins.sqldelight)
}

kotlin {
    js {
        browser()
        binaries.library()
    }
    wasmJs {
        browser()
        binaries.library()
    }
    sourceSets {
        commonMain.dependencies {
            implementation(libs.diva.lib.database)
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
