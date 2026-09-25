plugins {
    id("divabuild.app-lib-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core)
            api(projects.features.user.userModels)
            implementation(projects.features.auth.authModels)
            implementation(projects.features.session.sessionModels)

            implementation(projects.features.session.sessionCore)
            implementation(libs.diva.network)

            implementation(projects.features.user.userDatabase)
        }
    }
}
