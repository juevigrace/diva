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

include(":core")
include(":core-models")
include(":database")
include(":ui")

include(":features:auth:auth-core")
include(":features:auth:auth-models")

include(":features:devices:devices-core")
include(":features:devices:devices-database")
include(":features:devices:devices-models")

include(":features:onboarding:onboarding-core")

include(":features:permissions:permissions-core")
include(":features:permissions:permissions-database")
include(":features:permissions:permissions-models")

include(":features:session:session-core")
include(":features:session:session-database")
include(":features:session:session-models")

include(":features:settings:settings-core")

include(":features:user:user-core")
include(":features:user:user-database")
include(":features:user:user-models")

include(":features:verification:verification-core")
include(":features:verification:verification-models")

includeBuild("../framework")
