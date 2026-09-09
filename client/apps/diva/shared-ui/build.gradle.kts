plugins {
    id("divabuild.library-app-ui-shared")
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

            implementation(projects.core.models)
            implementation(projects.core.modelsApi)
            implementation(projects.core.database)
            implementation(projects.core.ui)
            implementation(projects.features.home)
            implementation(projects.features.server)
            implementation(projects.features.media)
            implementation(projects.features.folder)
            implementation(projects.features.collection)
            implementation(projects.features.playlist)
            implementation(projects.features.mix)
            implementation(projects.features.player)
            implementation(projects.features.library)
        }
    }
}
