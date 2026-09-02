@file:OptIn(ExperimentalWasmDsl::class)

import divabuild.internal.libs
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("divabuild.library-app")
    id("divabuild.cmp-deps")
    id("divabuild.serialization")
}

kotlin {
    js {
        browser()
        binaries.executable()
    }

    wasmJs {
        browser()
        binaries.executable()
    }
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            implementation(libs.diva.ui)
        }

        androidMain.dependencies {
            implementation(libs.koin.androidx.compose)
        }
    }
}
