@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension

plugins {
    id("divabuild.library-framework")
    id("divabuild.kmp-test")
}

// Fix for Webpack 5 tap error by ensuring compatible versions across all plugins
rootProject.plugins.withId("org.jetbrains.kotlin.multiplatform") {
    rootProject.extensions.configure<NodeJsRootExtension> {
        versions.webpack.version = "5.94.0"
        versions.webpackCli.version = "5.1.4"
    }
}

kotlin {
    js {
        browser {
            testTask {
                useKarma {
                    useFirefox()
                    useChromium()
                }
            }
        }
    }
    wasmJs {
        browser {
            testTask {
                useKarma {
                    useFirefox()
                    useChromium()
                }
            }
        }
    }
}
