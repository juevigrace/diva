import divabuild.internal.libs

plugins {
    id("divabuild.library-app-base")
}

kotlin {
    iosArm64 {
        binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }
    iosSimulatorArm64 {
        binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.compose.viewmodel)
        }
    }
}
