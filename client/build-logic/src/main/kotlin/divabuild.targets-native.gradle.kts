plugins {
    id("divabuild.kmp")
}

kotlin {
    watchosArm64()
    watchosSimulatorArm64()

    tvosArm64()
    tvosSimulatorArm64()

    macosArm64()
    iosX64()

    linuxX64()
    linuxArm64()

    mingwX64()
}

tasks.matching {
    it.name.startsWith("linkDebugTestMingwX64") ||
        it.name.startsWith("linkReleaseTestMingwX64") ||
        it.name == "mingwX64Test"
}.configureEach {
    onlyIf { System.getProperty("os.name").lowercase().contains("windows") }
}
