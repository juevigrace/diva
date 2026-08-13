pluginManagement {
    includeBuild("build-logic")
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
}

rootProject.name = "diva"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

include(
    ":app:apps:androidApp",
    ":app:apps:desktopApp",
    ":app:sharedUI",
)

include(
    ":app:core:database",
    ":app:core:ui",
)

include(
    ":app:core:models:models-core",
    ":app:core:models:models-api",
    ":app:core:models:models-shared",
)

include(
    ":app:features:app:home",
    ":app:features:app:onboarding",
    ":app:features:app:services",
    ":app:features:app:library",
    ":app:features:app:feed",
    ":app:features:app:creation",
    ":app:features:app:profile",
)

include(
    ":app:features:auth:auth-core",
    ":app:features:auth:auth-shared",
    ":app:features:auth:forgot",
    ":app:features:auth:session",
    ":app:features:auth:signin",
    ":app:features:auth:signup",
)

include(
    ":app:features:user",
    ":app:features:verification",
    ":app:features:permission",
)

include(
    ":framework:diva-core",
    ":framework:diva-database",
    ":framework:diva-database-test:diva-database-test-jvm",
    ":framework:diva-network-client",
    ":framework:diva-network-client-test",
    ":framework:diva-ui",
    ":framework:diva-ui-test",
)
