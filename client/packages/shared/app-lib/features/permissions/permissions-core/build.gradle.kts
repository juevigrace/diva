plugins {
    id("divabuild.app-lib-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core)
            implementation(projects.features.permissions.permissionsModels)

            implementation(libs.diva.network)

            implementation(projects.features.permissions.permissionsDatabase)
        }
    }
}
