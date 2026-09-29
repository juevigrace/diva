plugins {
    id("divabuild.diva-app-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.ui)

            implementation(projects.features.mix.mixDatabase)

            api(projects.features.collection.collectionModels)

            implementation(libs.diva.network)
        }
    }
}
