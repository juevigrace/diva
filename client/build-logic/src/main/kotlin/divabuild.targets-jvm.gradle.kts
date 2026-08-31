import divabuild.internal.libs

plugins {
    id("divabuild.kmp-base")
}

kotlin {
    jvm()

    sourceSets {
        jvmMain.dependencies {
            implementation(libs.logback.classic)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}

tasks.withType<Test>().configureEach {
    jvmArgs("--enable-native-access=ALL-UNNAMED")
}
