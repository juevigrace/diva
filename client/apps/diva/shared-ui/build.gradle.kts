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
            implementation(libs.diva.lib.core)
            implementation(libs.diva.lib.database)
            implementation(libs.diva.lib.ui)

            implementation(libs.diva.lib.auth)
            implementation(libs.diva.lib.user)

            implementation(libs.diva.network)
        }
    }
}
