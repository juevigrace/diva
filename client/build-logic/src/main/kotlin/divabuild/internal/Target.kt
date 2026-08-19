package divabuild.internal

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal fun KotlinMultiplatformExtension.hasTarget(name: String, configure: KotlinMultiplatformExtension.() -> Unit) {
    targets.matching { it.name == name }.configureEach {
        this@hasTarget.configure()
    }
}
