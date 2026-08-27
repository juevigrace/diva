@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("divabuild.library-framework")
    id("divabuild.kmp-test")
    alias(libs.plugins.sqldelight)
}

kotlin {
    js {
        browser {
            testTask {
                useKarma {
                    useChromeHeadless()
                    useFirefox()
                }
            }
        }
    }
    wasmJs {
        browser {
            testTask {
                useKarma {
                    useChromeHeadless()
                    useFirefox()
                }
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.divaCore)
            implementation(projects.divaDatabase)
            implementation(projects.divaDatabaseSqlite)
        }
        jsMain.dependencies {
            devNpm("copy-webpack-plugin", "9.1.0")
        }
        wasmJsMain.dependencies {
            devNpm("copy-webpack-plugin", "9.1.0")
        }
    }
}

dependencies {
    "androidHostTestImplementation"(libs.sqldelight.sqlite.driver)
    "androidHostTestImplementation"(libs.sqldelight.jdbc.driver)
    "androidDeviceTestImplementation"(libs.androidx.test.core)
}

sqldelight {
    databases {
        create("SqliteDB") {
            packageName.set("io.github.juevigrace.diva.database")
            schemaOutputDirectory.set(file("src/commonMain/sqldelight/databases"))
            srcDirs(file("src/commonMain/sqldelight/SqliteDB"))
            generateAsync.set(true)
        }
    }
}
