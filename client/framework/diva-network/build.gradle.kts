@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("divabuild.library-framework")
    id("divabuild.serialization")
}

kotlin {
    js {
        nodejs()
    }
    wasmJs {
        nodejs()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.divaCore)
            api(libs.ktor.client.core)
            api(libs.ktor.client.websockets)
            api(libs.ktor.client.logging)
        }
        androidMain.dependencies {
            api(libs.ktor.client.okhttp)
        }
        jvmMain.dependencies {
            api(libs.ktor.client.okhttp)
        }
        appleMain.dependencies {
            api(libs.ktor.client.darwin)
        }
        linuxMain.dependencies {
            api(libs.ktor.client.curl)
        }
        mingwMain.dependencies {
            api(libs.ktor.client.winhttp)
        }
        jsMain.dependencies {
            api(libs.ktor.client.js)
        }
        wasmJsMain.dependencies {
            api(libs.ktor.client.js)
        }
    }
}
