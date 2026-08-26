import divabuild.internal.libs
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    id("divabuild.cmp-deps")
    id("divabuild.targets-jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.compose.hot-reload")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.logger.slf4j)
        }
    }
}

compose {
    desktop {
        application {
            mainClass = "com.diva.app.MainKt"

            nativeDistributions {
                targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
                packageName = "com.diva.app"
                packageVersion = libs.versions.app.version.name.get()
            }
        }
    }
}
