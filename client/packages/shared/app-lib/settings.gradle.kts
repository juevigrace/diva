pluginManagement {
    includeBuild("../../../build-logic")
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
            from(files("../../../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "diva-lib"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

include(":diva-lib-core")
include(":diva-lib-database")
include(":diva-lib-ui")

include(":diva-lib-api-models")
include(":diva-lib-core-models")

include(":features:diva-lib-onboarding")
include(":features:diva-lib-auth")
include(":features:diva-lib-verification")
include(":features:diva-lib-session")
include(":features:diva-lib-user")
include(":features:diva-lib-settings")
include(":features:diva-lib-permissions")
include(":features:diva-lib-devices")

include(":features:diva-lib-session-database")
include(":features:diva-lib-user-database")
include(":features:diva-lib-permissions-database")
include(":features:diva-lib-devices-database")

includeBuild("../framework")
