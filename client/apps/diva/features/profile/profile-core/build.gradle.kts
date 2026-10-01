plugins {
    id("divabuild.diva-app-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.ui)

            implementation(projects.features.profile.profileModels)
            implementation(projects.features.media.mediaModels)

            implementation(libs.diva.lib.session.core)
            implementation(libs.diva.lib.session.models)

            implementation(libs.diva.lib.user.core)
            implementation(libs.diva.lib.settings.core)
            implementation(libs.diva.lib.devices.core)
            implementation(libs.diva.lib.permissions.core)
        }
    }
}
