import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.TestExecutable

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

    // Make Linux test executables find system libraries at runtime. Kotlin/Native
    // links libcrypt dynamically, so systems without a global loader cache need an
    // explicit runpath.
    targets.withType<KotlinNativeTarget>().configureEach {
        if (name.startsWith("linux")) {
            binaries.withType<TestExecutable>().configureEach {
                linkerOpts(
                    "-L/usr/lib",
                    "-L/usr/lib/x86_64-linux-gnu",
                    "-L/run/current-system/sw/lib",
                    "--rpath=/run/current-system/sw/lib",
                )
            }
        }
    }
}

tasks.matching {
    it.name.startsWith("linkDebugTestMingwX64") ||
        it.name.startsWith("linkReleaseTestMingwX64") ||
        it.name == "mingwX64Test"
}.configureEach {
    onlyIf { System.getProperty("os.name").lowercase().contains("windows") }
}
