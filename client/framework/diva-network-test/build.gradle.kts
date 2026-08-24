@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("divabuild.library-framework")
    id("divabuild.kmp-test")
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
            implementation(projects.divaNetwork)
        }
        commonTest.dependencies {
            implementation(libs.ktor.client.mock)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.kotlin.test)
        }
    }
}
