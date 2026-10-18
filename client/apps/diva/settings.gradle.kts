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
    ":resources",
    ":ui",
)

includeBuild("../../packages/shared/framework")

includeBuild("../../packages/shared/app-lib") {
    dependencySubstitution {
        val appLibModules =
            mapOf(
                "diva-lib-core" to ":core",
                "diva-lib-ui" to ":ui",
                "diva-lib-system-core" to ":features:system:system-core",
            )
        appLibModules.forEach { (moduleName, projectPath) ->
            substitute(module("io.github.juevigrace:$moduleName")).using(project(projectPath))
        }
    }
}
