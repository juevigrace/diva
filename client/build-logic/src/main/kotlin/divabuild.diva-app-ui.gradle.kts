import divabuild.internal.libs

plugins {
    id("divabuild.diva-app")
    id("divabuild.library-app-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.diva.lib.ui)
        }
    }
}
