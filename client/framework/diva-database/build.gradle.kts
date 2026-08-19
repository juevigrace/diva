plugins {
    id("divabuild.library-framework")
}

kotlin {
    js {
        browser()
    }
    wasmJs {
        browser()
    }
}
