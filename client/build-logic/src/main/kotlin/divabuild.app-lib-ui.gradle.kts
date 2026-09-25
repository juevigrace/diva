@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("divabuild.app-lib")
    id("divabuild.library-app-ui")
    id("divabuild.targets-web")
}

kotlin {
    js {
        browser()
    }

    wasmJs {
        browser()
    }
}
