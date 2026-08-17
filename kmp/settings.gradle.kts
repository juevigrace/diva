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

// framework and app are independent composite builds, stitched together here so
// app modules resolve io.github.juevigrace:diva-* from framework source.
includeBuild("framework")
includeBuild("app")

// NOTE: examples/diva-kmp-app is intentionally NOT part of this build.
// It is a standalone Gradle build (own settings.gradle.kts + build-logic) that
// consumes the published io.github.juevigrace:diva-* artifacts (version:
// io.github.juevigrace:diva-core:0.0.15 does not exist on Maven Central, the
// old ErrorCause/DivaAction API the samples use is only in 0.0.14 and earlier).
// Do NOT add includeBuild("examples/diva-kmp-app") here; the root build would
// fail on its dependency resolution. Build it separately with:
//     ./gradlew -p examples/diva-kmp-app ...