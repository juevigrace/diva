plugins {
    id("divabuild.kmp-base")
}

kotlin {
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        iosMain.dependencies {}
    }
}
