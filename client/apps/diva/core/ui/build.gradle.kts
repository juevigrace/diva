plugins {
    id("divabuild.library-app-ui")
    id("divabuild.serialization")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.diva.lib.ui)
        }
    }
}
