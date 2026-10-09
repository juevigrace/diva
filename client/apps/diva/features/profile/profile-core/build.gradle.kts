plugins {
    id("divabuild.diva-app-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.ui)

            implementation(libs.diva.lib.user.core)
            implementation(libs.diva.lib.user.models)
            implementation(libs.diva.lib.settings.core)
            implementation(libs.diva.lib.settings.models)
            implementation(libs.diva.lib.devices.core)
            implementation(libs.diva.lib.devices.models)
            implementation(libs.diva.lib.permissions.core)
            implementation(libs.diva.lib.permissions.models)
        }
    }
}
