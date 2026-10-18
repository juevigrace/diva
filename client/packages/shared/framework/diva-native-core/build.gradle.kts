plugins {
    id("divabuild.library-rust")
}

uniffi {
    generateFromLibrary {
        packageName = "io.github.juevigrace.diva.native"
    }
}

cargo {
    packageDirectory = layout.projectDirectory.dir("../../../../../rust/crates/diva-native")
}

kotlin {
    android {
        namespace = "io.github.juevigrace.diva.native.core"
    }
}
