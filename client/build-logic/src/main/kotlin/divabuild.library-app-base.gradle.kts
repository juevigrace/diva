import divabuild.internal.hasTarget
import divabuild.internal.libs

plugins {
    id("divabuild.cmp-deps")
    id("divabuild.targets-android")
    id("divabuild.targets-jvm")
    id("divabuild.targets-ios")
}

kotlin {
    android {
        namespace = "com.diva.app.${project.name.replace("-", ".")}"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
        }

        hasTarget("android") {
            androidMain.dependencies {
                implementation(libs.koin.android)
                implementation(libs.koin.androidx.compose)
            }
        }

        hasTarget("jvm") {
            jvmMain.dependencies {
                implementation(libs.koin.logger.slf4j)
            }
        }
    }
}
