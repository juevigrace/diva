import divabuild.internal.hasTarget
import divabuild.internal.libs

plugins {
    id("divabuild.cmp")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.runtime.saveable)
            implementation(libs.compose.animation)
            implementation(libs.compose.animation.graphics)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.foundation)
            implementation(libs.compose.ui)
            implementation(libs.compose.ui.util)
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.material3)
            implementation(libs.material3.adaptive)
            implementation(libs.material3.adaptive.layout)
            implementation(libs.material3.adaptive.nav3)
            implementation(libs.material3.adaptive.navigation.suite)
            implementation(libs.material3.window.size)
            implementation(libs.window.core)
            implementation(libs.nav3.ui)
            implementation(libs.savedstate)
            implementation(libs.savedstate.compose)
            implementation(libs.lifecycle.viewmodel)
            implementation(libs.lifecycle.viewmodel.savedstate)
            implementation(libs.lifecycle.viewmodel.nav3)
            implementation(libs.lifecycle.runtime.compose)
            implementation(libs.lifecycle.viewmodel.compose)
        }
    }

    hasTarget("android") {
        sourceSets {
            androidMain.dependencies {
                implementation(libs.androidx.activity.compose)
                implementation(libs.compose.ui.tooling)
            }
        }
    }

    hasTarget("jvm") {
        sourceSets {
            jvmMain.dependencies {
                implementation(libs.compose.desktop.common)
            }
        }
    }
}
