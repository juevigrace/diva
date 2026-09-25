@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("divabuild.app-lib")
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
            api(projects.coreModels)
            implementation(projects.features.session.sessionModels)
            implementation(projects.features.user.userModels)
        }
    }
}