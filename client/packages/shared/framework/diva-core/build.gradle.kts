@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("divabuild.framework-base")
}

kotlin {
    js {
        browser()
        nodejs()
        binaries.library()
    }
    wasmJs {
        browser()
        binaries.library()
    }
}
