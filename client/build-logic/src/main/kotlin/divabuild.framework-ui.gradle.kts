@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("divabuild.framework-base")
    id("divabuild.compose")
    id("divabuild.serialization")
}

kotlin {
    js {
        browser()
    }
    wasmJs {
        browser()
    }
}

tasks.configureEach {
    if (name.endsWith("BrowserTest") || name.startsWith("checkComposeUiTestConfigurationFor")) {
        enabled = false
    }
}
