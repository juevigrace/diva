plugins {
    id("divabuild.library-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.app.features.auth.authShared)
            implementation(projects.app.features.auth.forgot)
            api(projects.app.features.auth.session)
            implementation(projects.app.features.auth.signin)
            implementation(projects.app.features.auth.signup)
        }
    }
}
