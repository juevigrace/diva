plugins {
    id("divabuild.library-app")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.diva.database)
            api(libs.diva.database.sqlite)
        }
    }
}
