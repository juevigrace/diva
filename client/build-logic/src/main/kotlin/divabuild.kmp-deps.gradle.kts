import divabuild.internal.hasTarget
import divabuild.internal.libs

plugins {
    id("divabuild.kmp-base")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlin.reflect)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.savedstate)
        }

        hasTarget("android") {
            androidMain.dependencies {
                implementation(libs.kotlinx.coroutines.android)
            }
        }

        hasTarget("jvm") {
            jvmMain.dependencies {
                implementation(libs.kotlinx.coroutines.swing)
            }
        }

        hasTarget("js") {
            jsMain.dependencies {
                implementation(libs.kotlinx.coroutines.core.js)
            }
        }
    }
}
