plugins {
    id("divabuild.library-framework-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.divaCore)
        }
    }
}

compose.resources {
    generateResClass = never
    publicResClass = false
}
