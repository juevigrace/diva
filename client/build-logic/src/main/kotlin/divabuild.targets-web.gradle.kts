@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("divabuild.kmp-base")
}

kotlin {
    js {
        outputModuleName = project.name
        nodejs()
        generateTypeScriptDefinitions()
        compilerOptions {
            target = "es2015"
        }
    }

    wasmJs {
        outputModuleName = project.name
        nodejs()
        generateTypeScriptDefinitions()
        compilerOptions {
            target = "es2015"
        }
    }
}
