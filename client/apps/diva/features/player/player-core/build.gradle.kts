plugins {
    id("divabuild.diva-app-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.ui)

            implementation(projects.features.player.playerDatabase)
            implementation(projects.features.player.playerModels)
        }
    }
}
