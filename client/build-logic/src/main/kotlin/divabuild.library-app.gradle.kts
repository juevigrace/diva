@file:OptIn(ExperimentalWasmDsl::class)

import divabuild.internal.libs
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("divabuild.library-base")
    id("divabuild.library-version-apps")
}

kotlin {
    android {
        namespace = "io.github.juevigrace.${project.name.replace("-", ".")}"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)

            implementation(libs.diva.core)
        }

        androidMain.dependencies {
            implementation(libs.koin.android)
        }

        jvmMain.dependencies {
            implementation(libs.koin.logger.slf4j)
        }
    }
}
