plugins {
    id("divabuild.library-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.models)
            implementation(projects.core.database.databaseClient)
            implementation(projects.core.ui)

            implementation(projects.features.app.home)
            implementation(projects.features.app.library)
            implementation(projects.features.app.onboarding)
            implementation(projects.features.app.profile)
            implementation(projects.features.app.settings)

            implementation(projects.features.auth.authClient)
            implementation(projects.features.chat.chatClient)
            implementation(projects.features.collection.collectionClient)
            implementation(projects.features.media.mediaClient)
            implementation(projects.features.permissions.permissionsClient)
            implementation(projects.features.social.socialClient)
            implementation(projects.features.user.userClient)
            implementation(projects.features.verification.verificationClient)

            implementation(libs.diva.network.client)
            api(libs.koin.core)
        }

        androidMain.dependencies {
            api(libs.koin.android)
        }

        jvmMain.dependencies {
            api(libs.koin.logger.slf4j)
        }
    }
}
