plugins {
    id("divabuild.app-lib-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.ui)

            implementation(projects.features.permissions.permissionsDatabase)
            implementation(projects.features.permissions.permissionsModels)

            implementation(libs.diva.network)
        }
    }
}
