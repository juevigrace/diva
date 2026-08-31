plugins {
    id("divabuild.app-android")
}

android {
    namespace = "com.diva.app"

    defaultConfig {
        applicationId = "com.diva.app"
        versionCode = libs.versions.diva.app.code.get().toInt()
        versionName = libs.versions.diva.app.name.get()
    }
}
