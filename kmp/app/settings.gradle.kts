pluginManagement {
    includeBuild("../build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "app"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

include(
    ":apps:androidApp",
    ":apps:desktopApp",
    ":sharedUI",
)

include(
    ":core:database",
    ":core:ui",
)

include(
    ":core:models:models-core",
    ":core:models:models-api",
    ":core:models:models-shared",
)

include(
    ":features:app:home",
    ":features:app:onboarding",
    ":features:app:services",
    ":features:app:library",
    ":features:app:feed",
    ":features:app:creation",
    ":features:app:profile",
)

include(
    ":features:auth:auth-core",
    ":features:auth:auth-shared",
    ":features:auth:forgot",
    ":features:auth:session",
    ":features:auth:signin",
    ":features:auth:signup",
)

include(
    ":features:user",
    ":features:verification",
    ":features:permission",
)