plugins {
    id("divabuild.diva-app-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.ui)

            implementation(projects.features.collection.collectionDatabase)
            implementation(projects.features.collection.collectionModels)

            implementation(libs.diva.network)
        }
    }
}
