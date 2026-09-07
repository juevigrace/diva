@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("divabuild.library-app")
    id("divabuild.serialization")
}

kotlin {
    js {
        browser()
        nodejs()
        binaries.library()
    }
    wasmJs {
        browser()
        nodejs()
        binaries.library()
    }
    sourceSets {
        commonMain.dependencies {
            api(libs.diva.lib.models.api)
        }
    }
}
