plugins {
    id("divabuild.library-framework-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":diva-core"))
        }
    }
}

compose.resources {
    generateResClass = never
    publicResClass = false
}