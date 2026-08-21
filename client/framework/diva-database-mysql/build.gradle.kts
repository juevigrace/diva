plugins {
    id("divabuild.library-framework-jvm")
}

dependencies {
    api(projects.divaDatabase)
    api(libs.sqldelight.jdbc.driver)
    api(libs.hikaricp)
    api(libs.mysql)
}
