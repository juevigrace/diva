@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("divabuild.library-framework-ui")
}

kotlin {
    js {
        binaries.library()
    }
    wasmJs {
        binaries.library()
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.divaCore)
        }
    }
}

compose.resources {
    generateResClass = never
    publicResClass = false
}
