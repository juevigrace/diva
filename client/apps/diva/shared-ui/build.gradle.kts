plugins {
    id("divabuild.diva-app-ui")
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
            implementation(projects.ui)

            implementation(libs.diva.lib.system.core)
        }
    }
}
