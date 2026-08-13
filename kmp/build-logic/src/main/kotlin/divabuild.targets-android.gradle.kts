import divabuild.internal.cleanPath
import divabuild.internal.libs
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("divabuild.kmp-base")
    id("com.android.kotlin.multiplatform.library")
}

val isFrameworkModule = path.startsWith(":framework")

kotlin {
    android {
        namespace = if (isFrameworkModule) {
            "io.github.juevigrace.${project.name.split("-").joinToString(".")}"
        } else {
            "com.diva.${cleanPath()}"
        }

        compileSdk = libs.versions.android.compileSdk.get().toInt()

        minSdk = libs.versions.android.minSdk.get().toInt()

        withJava()
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }

        if (!isFrameworkModule) {
            androidResources.enable = true
        }
    }

    sourceSets {
        if (isFrameworkModule) {
            androidMain.dependencies {
                api(libs.androidx.core.ktx)
            }
        }
    }
}
