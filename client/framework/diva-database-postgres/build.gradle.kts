plugins {
    id("divabuild.library-framework-jvm")
}

dependencies {
    api(project(":diva-database"))
    api(libs.sqldelight.jdbc.driver)
    api(libs.sqldelight.r2dbc.driver)
    api(libs.hikaricp)
    api(libs.postgresql)
}
