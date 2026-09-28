@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("divabuild.library-app")
    id("divabuild.diva-app")
}

kotlin {
    js {
        browser()
        nodejs()
        binaries.library()
    }
    wasmJs {
        browser()
        nodejs()
        binaries.library()
    }
    sourceSets {
        commonMain.dependencies {
            api(libs.diva.lib.core)
            api(projects.features.media.mediaModels)
            api(projects.features.folder.folderModels)
            api(projects.features.collection.collectionModels)
        }
    }
}