pluginManagement {
    includeBuild("../../build-logic")
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
            from(files("../../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "diva-app"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

include(
    ":platforms:androidApp",
    ":platforms:desktopApp",
)

include(":shared-ui")

include(
    ":core",
    ":core-models",
    ":api-models",
    ":database",
    ":resources",
    ":ui",
    ":features:home",
    ":features:library",
    ":features:media",
    ":features:folder",
    ":features:collection",
    ":features:playlist",
    ":features:mix",
    ":features:player",
    ":features:server",
    ":features:search",
    ":features:profile",
)

includeBuild("../../packages/shared/framework")
includeBuild("../../packages/shared/app-lib")
