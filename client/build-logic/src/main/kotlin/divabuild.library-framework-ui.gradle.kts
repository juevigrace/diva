import divabuild.internal.libs

plugins {
    id("divabuild.library-framework-base")
    id("divabuild.serialization")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
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
            implementation(libs.nav3)
            implementation(libs.nav3.ui)
            implementation(libs.savedstate)
            implementation(libs.savedstate.compose)
            implementation(libs.window.core)
            implementation(libs.lifecycle.viewmodel)
            implementation(libs.lifecycle.runtime.compose)
            implementation(libs.lifecycle.viewmodel.compose)
            implementation(libs.lifecycle.viewmodel.savedstate)
            implementation(libs.lifecycle.viewmodel.nav3)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
        }
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
            implementation(libs.compose.ui.tooling)
        }
        jvmMain.dependencies {
            implementation(libs.compose.desktop.common)
            implementation(libs.koin.logger.slf4j)
        }
    }
}
