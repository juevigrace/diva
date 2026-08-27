import divabuild.internal.hasTarget
import divabuild.internal.libs

plugins {
    id("divabuild.kmp-base")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.koin.core)
            api(libs.koin.compose)
            api(libs.koin.compose.viewmodel)
        }

        hasTarget("android") {
            androidMain.dependencies {
                api(libs.koin.android)
                api(libs.koin.androidx.compose)
            }
        }

        hasTarget("jvm") {
            jvmMain.dependencies {
                api(libs.koin.logger.slf4j)
            }
        }
    }
}
