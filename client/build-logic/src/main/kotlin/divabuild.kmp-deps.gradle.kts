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
            implementation(libs.nav3)
            implementation(libs.window.core)
            implementation(libs.lifecycle.viewmodel)
            implementation(libs.lifecycle.viewmodel.savedstate)
            implementation(libs.lifecycle.viewmodel.nav3)
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