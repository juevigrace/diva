@file:OptIn(ExperimentalWasmDsl::class)

import divabuild.internal.libs
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin

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
            optIn.add("kotlin.js.ExperimentalJsExport")
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

rootProject.plugins.withType<NodeJsRootPlugin> {
    rootProject.extensions.configure<NodeJsRootExtension> {
        versions.webpack.version = "5.112.2"
    }
}
