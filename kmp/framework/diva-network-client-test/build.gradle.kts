plugins {
    id("divabuild.library-package")
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(projects.divaCore)
    implementation(projects.divaNetworkClient)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.koin.test)
    testImplementation(libs.kotlinx.serialization.json)
    testImplementation(libs.ktor.client.mock)
}
