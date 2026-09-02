plugins {
    id("divabuild.library-app")
}

kotlin {
    js {
        nodejs()
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.divaLibModelsCore)
        }
    }
}
