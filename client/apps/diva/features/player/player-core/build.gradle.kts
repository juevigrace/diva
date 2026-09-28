plugins {
    id("divabuild.diva-app-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.diva.lib.core)
            implementation(libs.diva.lib.database)

            implementation(projects.core)
            implementation(projects.features.player.playerDatabase)

            api(projects.features.media.mediaModels)
            api(projects.features.player.playerModels)

            implementation(libs.diva.network)
        }
    }
}
