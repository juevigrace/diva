import divabuild.internal.libs
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    kotlin("jvm")
    id("divabuild.library-version-framework")
}

group = "io.github.juevigrace"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

kotlin {
    compilerOptions {
        apiVersion = KotlinVersion.KOTLIN_2_4
        languageVersion = KotlinVersion.KOTLIN_2_4
        progressiveMode = languageVersion.map { it >= KotlinVersion.DEFAULT }
        jvmTarget = JvmTarget.JVM_21
    }
}

dependencies {
    implementation(libs.logback.classic)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.swing)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.datetime)
}
