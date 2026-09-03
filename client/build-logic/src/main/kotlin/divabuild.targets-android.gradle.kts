import divabuild.internal.buildLogicResourcesDir
import divabuild.internal.libs
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("divabuild.kmp-base")
    id("com.android.kotlin.multiplatform.library")
}

kotlin {
    android {
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        optimization {
            minify = true
            consumerKeepRules.apply {
                publish = true
                file(buildLogicResourcesDir().resolve("consumer-rules.pro"))
            }
        }

        withJava()
        compilerOptions {
            jvmTarget = JvmTarget.JVM_21
        }
        androidResources {
            enable = true
        }
        withHostTest {
            isIncludeAndroidResources = true
        }
        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.core.ktx)
            implementation(libs.kotlinx.coroutines.android)
        }
    }
}
