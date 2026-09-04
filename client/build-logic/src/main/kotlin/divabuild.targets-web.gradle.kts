@file:OptIn(ExperimentalWasmDsl::class)

import divabuild.internal.libs
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("divabuild.kmp-base")
}

kotlin {
    js {
        outputModuleName = project.name
        generateTypeScriptDefinitions()
        compilerOptions {
            target = "es2015"
            freeCompilerArgs.add("-Xes-long-as-bigint")
        }
    }

    wasmJs {
        outputModuleName = project.name
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
