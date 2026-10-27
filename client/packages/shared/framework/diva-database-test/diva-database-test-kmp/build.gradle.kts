@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("divabuild.framework")
    id("divabuild.test")
    alias(libs.plugins.sqldelight)
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

    linuxX64 {
        binaries.getTest("DEBUG").linkerOpts("-lsqlite3", "--allow-shlib-undefined")
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.divaCore)
            implementation(projects.divaDatabase)
            implementation(projects.divaDatabaseSqlite)
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
            generateAsync.set(true)
            deriveSchemaFromMigrations.set(true)
        }
    }
}
