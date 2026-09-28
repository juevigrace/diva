import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    id("divabuild.app-desktop")
}

dependencies {
    implementation(projects.sharedUi)
}

compose {
    desktop {
        application {
            mainClass = "com.diva.app.MainKt"

            nativeDistributions {
                targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.AppImage)
                packageName = "com.diva.app"
                packageVersion = libs.versions.diva.app.name.get()
            }
        }
    }
}
