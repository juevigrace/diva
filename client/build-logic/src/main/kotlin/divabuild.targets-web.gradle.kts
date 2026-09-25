@file:OptIn(ExperimentalWasmDsl::class)

import divabuild.internal.libs
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("divabuild.kmp")
}

kotlin {
    js {
        outputModuleName = project.base.archivesName
        generateTypeScriptDefinitions()
        compilerOptions {
            target = "es2015"
            freeCompilerArgs.add("-Xes-long-as-bigint")
        }
    }

    wasmJs {
        outputModuleName = project.base.archivesName
        generateTypeScriptDefinitions()
        compilerOptions {
            target = "es2015"
        }
    }

    sourceSets {
        jsMain.dependencies {
            implementation(libs.kotlinx.coroutines.core.js)
        }
    }
}
