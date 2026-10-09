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
    ":database",
    ":resources",
    ":ui",
    ":features:home:home-core",
    ":features:library:library-core",
    ":features:media:media-core",
    ":features:media:media-models",
    ":features:folder:folder-core",
    ":features:folder:folder-models",
    ":features:collection:collection-core",
    ":features:collection:collection-models",
    ":features:playlist:playlist-core",
    ":features:mix:mix-core",
    ":features:player:player-core",
    ":features:player:player-models",
    ":features:server:server-core",
    ":features:server:server-models",
    ":features:search:search-core",
    ":features:search:search-models",
    ":features:settings:settings-core",
    ":features:settings:settings-database",
    ":features:settings:settings-models",
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
                "diva-lib-database" to ":database",
                "diva-lib-ui" to ":ui",
                "diva-lib-auth-core" to ":features:auth:auth-core",
                "diva-lib-auth-models" to ":features:auth:auth-models",
                "diva-lib-devices-core" to ":features:devices:devices-core",
                "diva-lib-devices-database" to ":features:devices:devices-database",
                "diva-lib-devices-models" to ":features:devices:devices-models",
                "diva-lib-onboarding-core" to ":features:onboarding:onboarding-core",
                "diva-lib-permissions-core" to ":features:permissions:permissions-core",
                "diva-lib-permissions-database" to ":features:permissions:permissions-database",
                "diva-lib-permissions-models" to ":features:permissions:permissions-models",
                "diva-lib-session-core" to ":features:session:session-core",
                "diva-lib-session-database" to ":features:session:session-database",
                "diva-lib-session-models" to ":features:session:session-models",
                "diva-lib-settings-core" to ":features:settings:settings-core",
                "diva-lib-settings-models" to ":features:settings:settings-models",
                "diva-lib-user-core" to ":features:user:user-core",
                "diva-lib-user-database" to ":features:user:user-database",
                "diva-lib-user-models" to ":features:user:user-models",
                "diva-lib-verification-core" to ":features:verification:verification-core",
                "diva-lib-verification-models" to ":features:verification:verification-models",
            )
        appLibModules.forEach { (moduleName, projectPath) ->
            substitute(module("io.github.juevigrace:$moduleName")).using(project(projectPath))
        }
    }
}
