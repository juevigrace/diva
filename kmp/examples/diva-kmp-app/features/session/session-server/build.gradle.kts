plugins {
    id("divabuild.library-server")
}

dependencies {
    implementation(projects.core.database.databaseServer)
    implementation(projects.core.models)
}
