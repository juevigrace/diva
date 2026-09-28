plugins {
    id("divabuild.app-lib-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.features.verification.verificationModels)

            implementation(libs.diva.network)
        }
    }
}
