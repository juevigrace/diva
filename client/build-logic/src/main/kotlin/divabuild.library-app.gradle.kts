@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("divabuild.library-base")
    id("divabuild.cmp-deps")
    id("divabuild.koin-deps")
    id("divabuild.library-version-app")
}

group = "io.github.juevigrace.diva.lib"

kotlin {
    android {
        namespace = "io.github.juevigrace.diva.lib.${project.name.replace("-", ".")}"
    }

    js {
        browser()
    }

    wasmJs {
        browser()
    }
}
