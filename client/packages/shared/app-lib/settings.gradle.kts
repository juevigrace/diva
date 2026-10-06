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
include(":database")
include(":ui")

include(
    ":features:auth:auth-core",
    ":features:auth:auth-models"
)

include(
    ":features:devices:devices-core",
    ":features:devices:devices-database",
    ":features:devices:devices-models"
)

include(":features:onboarding:onboarding-core")

include(
    ":features:permissions:permissions-core",
    ":features:permissions:permissions-database",
    ":features:permissions:permissions-models"
)

include(
    ":features:session:session-core",
    ":features:session:session-database",
    ":features:session:session-models"
)

include(
    ":features:settings:settings-core",
    ":features:settings:settings-models"
)

include(
    ":features:user:user-core",
    ":features:user:user-database",
    ":features:user:user-models"
)

include(
    ":features:verification:verification-core",
    ":features:verification:verification-models"
)

includeBuild("../framework")
