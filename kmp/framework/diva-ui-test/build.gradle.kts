plugins {
    id("divabuild.library-base")
    id("divabuild.library-targets")
    id("divabuild.serialization")
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.framework.divaCore)
            implementation(projects.framework.divaUi)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
