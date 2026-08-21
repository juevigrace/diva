@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("divabuild.library-framework-ui")
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
            api(projects.divaCore)
        }
    }
}

compose.resources {
    generateResClass = never
    publicResClass = false
}
