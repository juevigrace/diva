plugins {
    id("divabuild.kmp-base")
}

kotlin {
    mingwX64()
}

tasks.configureEach {
    listOf("linkDebugTestMingwX64", "linkReleaseTestMingwX64", "mingwX64Test", "compileTestKotlinMingwX64")
        .forEach { t ->
            if (name.startsWith(t)) {
                onlyIf { System.getProperty("os.name").lowercase().contains("windows") }
            }
        }
}
