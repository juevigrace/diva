import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform")
}

kotlin {
    compilerOptions {
        apiVersion = KotlinVersion.KOTLIN_2_4
        languageVersion = KotlinVersion.KOTLIN_2_4
        progressiveMode = languageVersion.map { it >= KotlinVersion.DEFAULT }
        freeCompilerArgs.addAll("-Xexpect-actual-classes")
    }
}

tasks.withType<KotlinCompile> {
    compilerOptions.jvmTarget.set(JvmTarget.JVM_21)
}
