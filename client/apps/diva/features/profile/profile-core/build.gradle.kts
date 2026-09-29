plugins {
    id("divabuild.diva-app-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.ui)

            implementation(projects.features.profile.profileModels)

            implementation(libs.diva.lib.session.core)
            implementation(libs.diva.lib.session.models)

            implementation(libs.diva.lib.user.core)
        }
    }
}
