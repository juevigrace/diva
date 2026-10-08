plugins {
    id("divabuild.app-lib-ui")
}

compose.resources {
    generateResClass = always
    publicResClass = true
    packageOfResClass = "io.github.juevigrace.diva.lib.generated.resources"
}
