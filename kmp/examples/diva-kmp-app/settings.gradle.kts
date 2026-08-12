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
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        mavenLocal()
    }
}

rootProject.name = "diva-kmp-app"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

fun shouldIncludeClientProjects(): Boolean {
    return !(providers.gradleProperty("excludeCompose").orNull?.toBoolean() ?: false)
}

// Client projects
if (shouldIncludeClientProjects()) {
    include(
        ":apps:androidApp",
        ":apps:desktopApp",
        ":apps:sharedUI",
    )

    include(
        ":core:database:database-client",
        ":core:resources",
        ":core:ui"
    )

    include(
        ":features:app:home",
        ":features:app:library",
        ":features:app:onboarding",
        ":features:app:profile",
        ":features:app:settings",
    )

    include(
        ":features:auth:auth-client",
        ":features:chat:chat-client",
        ":features:collection:collection-client",
        ":features:media:media-client",
        ":features:permissions:permissions-client",
        ":features:session:session-client",
        ":features:social:social-client",
        ":features:user:user-client",
        ":features:verification:verification-client",
    )
}

// Server projects
include(":server")

include(":core:server:util")
include(":core:server:mail")

include(":core:database:database-server")

include(":core:models-server")

include(
    ":features:auth:auth-server",
    ":features:chat:chat-server",
    ":features:collection:collection-server",
    ":features:media:media-server",
    ":features:permissions:permissions-server",
    ":features:session:session-server",
    ":features:social:social-server",
    ":features:user:user-server",
    ":features:verification:verification-server",
)

// Shared projects
include(
    ":core:database:database-shared"
)

include(
    ":core:models",
    ":core:models:models-shared",
    ":core:models:models-api",
)
