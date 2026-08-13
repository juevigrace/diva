plugins {
    id("divabuild.library-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.app.core.database)
            implementation(projects.app.core.ui)

            implementation(projects.app.features.app.home)
            implementation(projects.app.features.app.onboarding)

            implementation(projects.app.features.app.services)

            implementation(projects.app.features.auth.authCore)

            implementation(projects.app.features.user)

            implementation(projects.app.features.verification)

            implementation(projects.app.features.permission)

            implementation(libs.diva.network.client)
        }

        jvmMain.dependencies {
            api(libs.koin.logger.slf4j)
        }
    }
}
