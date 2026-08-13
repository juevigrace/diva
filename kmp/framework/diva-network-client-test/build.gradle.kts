plugins {
    id("divabuild.library-package")
    alias(libs.plugins.kotlin.jvm)
    id("org.jetbrains.kotlin.plugin.serialization")

}

        dependencies {
            implementation(projects.framework.divaCore)
            implementation(projects.framework.divaNetworkClient)

            testImplementation(libs.kotlin.test)
            testImplementation(libs.kotlinx.coroutines.test)
            testImplementation(libs.koin.test)
            testImplementation(libs.kotlinx.serialization.json)
            testImplementation(libs.ktor.client.mock)
}
