import divabuild.internal.buildLogicResourcesDir
import divabuild.internal.libs
import org.jetbrains.compose.reload.gradle.ComposeHotRun

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.compose.hot-reload")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

compose.desktop {
    application {
        buildTypes.release.proguard {
            version.set(libs.versions.proguard.get())
            configurationFiles.from(
                buildLogicResourcesDir().resolve("proguard-rules.pro"),
                project.file("proguard-rules.pro"),
            )
        }
    }
}

tasks.withType<ComposeHotRun>().configureEach {
    jvmArgs("--enable-native-access=ALL-UNNAMED")
}

tasks.withType<JavaExec>().configureEach {
    jvmArgs("--enable-native-access=ALL-UNNAMED")
}

dependencies {
    implementation(compose.desktop.currentOs)

    implementation(libs.logback.classic)

    implementation(libs.koin.compose)
    implementation(libs.koin.compose.viewmodel)
    implementation(libs.koin.core)
    implementation(libs.koin.logger.slf4j)

    implementation(libs.kotlin.reflect)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.swing)
    implementation(libs.kotlinx.datetime)
}
