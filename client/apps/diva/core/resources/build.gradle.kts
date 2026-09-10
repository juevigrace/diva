plugins {
    id("divabuild.library-app-ui-shared")
}

compose.resources {
    generateResClass = always
    publicResClass = true
    packageOfResClass = "com.diva.app.generated.resources"
}