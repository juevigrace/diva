@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("divabuild.library-framework")
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
            api(project(":diva-database"))
        }
        androidMain.dependencies {
            api(libs.sqldelight.android.driver)
        }
        nativeMain.dependencies {
            api(libs.sqldelight.native.driver)
        }
        jvmMain.dependencies {
            api(libs.sqldelight.sqlite.driver)
            api(libs.sqldelight.jdbc.driver)
            implementation(libs.sqlite)
        }
        jsMain.dependencies {
            api(libs.sqldelight.web.worker.driver)
            api(npm("@cashapp/sqldelight-sqljs-worker", "2.3.2"))
            api(devNpm("copy-webpack-plugin", "9.1.0"))
            api(npm("sql.js", "1.8.0"))
        }
        wasmJsMain.dependencies {
            api(libs.sqldelight.web.worker.driver.wasm.js)
            api(npm("@cashapp/sqldelight-sqljs-worker", "2.3.2"))
            api(devNpm("copy-webpack-plugin", "9.1.0"))
            api(npm("sql.js", "1.8.0"))
        }
    }
}
