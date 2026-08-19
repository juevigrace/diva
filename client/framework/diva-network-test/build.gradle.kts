plugins {
    id("divabuild.library-framework")
    id("divabuild.kmp-test")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":diva-core"))
            implementation(project(":diva-network"))
        }
        commonTest.dependencies {
            implementation(libs.ktor.client.mock)
        }
    }
}