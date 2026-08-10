import divabuild.internal.libs

plugins {
    id("divabuild.app-package")
    id("org.jetbrains.kotlin.jvm")
    `java-library`
}

dependencies {
    implementation(libs.diva.core)
}
