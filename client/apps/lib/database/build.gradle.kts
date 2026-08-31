plugins {
    id("divabuild.library-app")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.diva.database)
        }
    }
}
