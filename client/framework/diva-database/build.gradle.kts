@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("divabuild.library-framework")
}

kotlin {
    js {
        browser()
        nodejs()
    }
    wasmJs {
        browser()
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.divaCore)
            api(libs.sqldelight.async.extensions)
            api(libs.sqldelight.coroutines.extensions)
        }
    }
}
