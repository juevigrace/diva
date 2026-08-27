@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("divabuild.library-framework-base")
    id("divabuild.cmp-deps")
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
