plugins {
    id("divabuild.library-framework-ui")
    id("divabuild.kmp-test")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":diva-core"))
            implementation(project(":diva-ui"))
        }
        commonTest.dependencies {
            implementation(libs.compose.ui.test)
        }
        jvmTest.dependencies {
            implementation(libs.compose.ui.test.junit4)
        }
    }
}