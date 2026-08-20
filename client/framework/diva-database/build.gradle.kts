plugins {
    id("divabuild.library-framework")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":diva-core"))
            api(libs.sqldelight.async.extensions)
            api(libs.sqldelight.coroutines.extensions)
        }
    }
}
