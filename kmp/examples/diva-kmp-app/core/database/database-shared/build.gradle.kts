plugins {
    id("divabuild.library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.models)

            api(libs.diva.database)
        }
    }
}
