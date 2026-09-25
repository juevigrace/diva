import divabuild.internal.libs

plugins {
    id("divabuild.library-app-shared")
    id("divabuild.compose")
    id("divabuild.serialization")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            implementation(libs.diva.ui)
        }

        androidMain.dependencies {
            implementation(libs.koin.androidx.compose)
        }
    }
}
