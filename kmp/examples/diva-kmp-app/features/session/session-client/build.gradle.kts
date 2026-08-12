plugins {
    id("divabuild.library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.database.databaseClient)

            implementation(projects.core.models)
        }
    }
}
