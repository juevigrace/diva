@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("divabuild.library-framework-ui")
    id("divabuild.kmp-test")
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
            implementation(projects.divaCore)
            implementation(projects.divaUi)
        }
        commonTest.dependencies {
            implementation(libs.compose.ui.test)
        }
        jvmTest.dependencies {
            implementation(libs.compose.ui.test.junit4)
        }
    }
}
