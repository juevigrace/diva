import divabuild.internal.libs

plugins {
    id("divabuild.library-base")
    id("divabuild.library-version-apps")
}

kotlin {
    android {
        namespace = "io.github.juevigrace.${project.name.replace("-", ".")}"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)

            implementation(libs.diva.core)
        }

        androidMain.dependencies {
            implementation(libs.koin.android)
        }

        jvmMain.dependencies {
            implementation(libs.koin.logger.slf4j)
        }
    }
}
