plugins {
    id("divabuild.library-framework-ui")
    id("divabuild.kmp-test")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.divaCore)
            implementation(projects.divaUi)
        }
    }
}
