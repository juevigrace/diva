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

include(":features:collection-database")
project(":features:collection-database").projectDir = file("features/collection/database")

include(":features:folder-database")
project(":features:folder-database").projectDir = file("features/folder/database")

include(":features:library-database")
project(":features:library-database").projectDir = file("features/library/database")

include(":features:media-database")
project(":features:media-database").projectDir = file("features/media/database")

include(":features:mix-database")
project(":features:mix-database").projectDir = file("features/mix/database")

include(":features:player-database")
project(":features:player-database").projectDir = file("features/player/database")

include(":features:playlist-database")
project(":features:playlist-database").projectDir = file("features/playlist/database")

include(":features:server-database")
project(":features:server-database").projectDir = file("features/server/database")

includeBuild("../../packages/shared/framework")
includeBuild("../../packages/shared/app-lib")
