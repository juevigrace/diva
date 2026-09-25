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
    ":features:home:home-core",
    ":features:library:library-core",
    ":features:media:media-core",
    ":features:folder:folder-core",
    ":features:collection:collection-core",
    ":features:playlist:playlist-core",
    ":features:mix:mix-core",
    ":features:player:player-core",
    ":features:server:server-core",
    ":features:search:search-core",
    ":features:profile:profile-core",
)

include(":features:collection:collection-database")

include(":features:folder:folder-database")

include(":features:library:library-database")

include(":features:media:media-database")

include(":features:mix:mix-database")

include(":features:player:player-database")

include(":features:playlist:playlist-database")

include(":features:server:server-database")

includeBuild("../../packages/shared/framework")

includeBuild("../../packages/shared/app-lib") {
    dependencySubstitution {
        val appLibModules =
            mapOf(
                "diva-lib-core" to ":core",
                "diva-lib-core-models" to ":core-models",
                "diva-lib-database" to ":database",
                "diva-lib-ui" to ":ui",
                "diva-lib-auth" to ":features:auth:auth-core",
                "diva-lib-auth-models" to ":features:auth:auth-models",
                "diva-lib-devices" to ":features:devices:devices-core",
                "diva-lib-devices-database" to ":features:devices:devices-database",
                "diva-lib-devices-models" to ":features:devices:devices-models",
                "diva-lib-onboarding" to ":features:onboarding:onboarding-core",
                "diva-lib-permissions" to ":features:permissions:permissions-core",
                "diva-lib-permissions-database" to ":features:permissions:permissions-database",
                "diva-lib-permissions-models" to ":features:permissions:permissions-models",
                "diva-lib-session" to ":features:session:session-core",
                "diva-lib-session-database" to ":features:session:session-database",
                "diva-lib-session-models" to ":features:session:session-models",
                "diva-lib-settings" to ":features:settings:settings-core",
                "diva-lib-user" to ":features:user:user-core",
                "diva-lib-user-database" to ":features:user:user-database",
                "diva-lib-user-models" to ":features:user:user-models",
                "diva-lib-verification" to ":features:verification:verification-core",
                "diva-lib-verification-models" to ":features:verification:verification-models",
            )
        appLibModules.forEach { (moduleName, projectPath) ->
            substitute(module("io.github.juevigrace:$moduleName")).using(project(projectPath))
        }
    }
}
