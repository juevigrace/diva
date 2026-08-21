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

rootProject.name = "framework"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

include(":diva-core")
include(":diva-database")
include(":diva-database-sqlite")
include(":diva-database-postgres")
include(":diva-database-mysql")
include(":diva-database-test:diva-database-test-kmp")
include(":diva-database-test:diva-database-test-jvm")
include(":diva-network")
include(":diva-network-test")
include(":diva-ui")
include(":diva-ui-test")
