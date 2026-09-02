plugins {
    id("divabuild.library-app")
    id("divabuild.serialization")
}

kotlin {
    js {
        browser()
        nodejs()
        binaries.library()
    }

    wasmJs {
        browser()
        nodejs()
        binaries.library()
    }
}
