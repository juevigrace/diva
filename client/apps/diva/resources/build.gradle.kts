plugins {
    id("divabuild.diva-app-ui")
}

compose.resources {
    generateResClass = always
    publicResClass = true
    packageOfResClass = "com.diva.app.generated.resources"
}
