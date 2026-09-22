import divabuild.internal.libs

plugins {
    id("divabuild.library-base")
}

kotlin {
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
