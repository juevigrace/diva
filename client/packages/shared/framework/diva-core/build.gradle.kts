@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("divabuild.framework")
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
