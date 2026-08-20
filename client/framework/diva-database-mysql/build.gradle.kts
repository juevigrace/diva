plugins {
    id("divabuild.library-framework-jvm")
}

dependencies {
    api(project(":diva-database"))
    api(libs.sqldelight.jdbc.driver)
    api(libs.hikaricp)
    api(libs.mysql)
}
