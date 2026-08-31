plugins {
    id("divabuild.library-app-ui")
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.core)
            implementation(projects.database)
            implementation(projects.ui)

            implementation(projects.features.auth)
            implementation(projects.features.user)

            implementation(libs.diva.network)
        }
    }
}
