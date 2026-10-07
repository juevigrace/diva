plugins {
    id("divabuild.diva-app-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.ui)

            implementation(projects.features.player.playerDatabase)
            implementation(projects.features.player.playerModels)

            implementation(libs.diva.lib.session.core)
            implementation(libs.diva.lib.session.models)
        }
    }
}
