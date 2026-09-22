plugins {
    id("divabuild.diva-app-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.diva.lib.ui)

            api(projects.resources)
            implementation(projects.core)
        }
    }
}
