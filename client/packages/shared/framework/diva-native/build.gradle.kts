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

    sourceSets {
        jvmMain.dependencies {
            implementation(projects.divaNativeCore)
        }

        androidMain.dependencies {
            implementation(projects.divaNativeCore)
        }

        iosMain.dependencies {
            implementation(projects.divaNativeCore)
        }

        jvmTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}
