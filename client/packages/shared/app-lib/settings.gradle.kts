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
project(":diva-lib-core").projectDir = file("core")

include(":diva-lib-database")
project(":diva-lib-database").projectDir = file("database")

include(":diva-lib-ui")
project(":diva-lib-ui").projectDir = file("ui")

include(":diva-lib-api-models")
project(":diva-lib-api-models").projectDir = file("api-models")

include(":diva-lib-core-models")
project(":diva-lib-core-models").projectDir = file("core-models")

include(":features:diva-lib-onboarding")
project(":features:diva-lib-onboarding").projectDir = file("features/onboarding")

include(":features:diva-lib-auth")
project(":features:diva-lib-auth").projectDir = file("features/auth")

include(":features:diva-lib-verification")
project(":features:diva-lib-verification").projectDir = file("features/verification")

include(":features:diva-lib-session")
project(":features:diva-lib-session").projectDir = file("features/session")

include(":features:diva-lib-user")
project(":features:diva-lib-user").projectDir = file("features/user")

include(":features:diva-lib-settings")
project(":features:diva-lib-settings").projectDir = file("features/settings")

include(":features:diva-lib-permissions")
project(":features:diva-lib-permissions").projectDir = file("features/permissions")

include(":features:diva-lib-devices")
project(":features:diva-lib-devices").projectDir = file("features/devices")

include(":features:diva-lib-session-database")
project(":features:diva-lib-session-database").projectDir = file("features/session/database")

include(":features:diva-lib-user-database")
project(":features:diva-lib-user-database").projectDir = file("features/user/database")

include(":features:diva-lib-permissions-database")
project(":features:diva-lib-permissions-database").projectDir = file("features/permissions/database")

include(":features:diva-lib-devices-database")
project(":features:diva-lib-devices-database").projectDir = file("features/devices/database")

includeBuild("../framework")