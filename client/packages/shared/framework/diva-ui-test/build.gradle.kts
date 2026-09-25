plugins {
    id("divabuild.framework-ui")
    id("divabuild.test")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.divaCore)
            implementation(projects.divaUi)
        }
    }
}
