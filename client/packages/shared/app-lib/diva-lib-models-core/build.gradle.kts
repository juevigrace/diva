plugins {
    id("divabuild.library-app")
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
            implementation(projects.divaLibModelsApi)
        }
    }
}
