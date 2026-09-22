plugins {
    id("divabuild.diva-app-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.diva.lib.core)
            implementation(libs.diva.lib.user)
            implementation(libs.diva.lib.session)

            implementation(projects.core)
        }
    }
}
