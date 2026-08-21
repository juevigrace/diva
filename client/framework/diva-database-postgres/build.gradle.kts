plugins {
    id("divabuild.library-framework-jvm")
}

dependencies {
    api(projects.divaDatabase)
    api(libs.sqldelight.jdbc.driver)
    api(libs.sqldelight.r2dbc.driver)
    api(libs.hikaricp)
    api(libs.postgresql)
}
