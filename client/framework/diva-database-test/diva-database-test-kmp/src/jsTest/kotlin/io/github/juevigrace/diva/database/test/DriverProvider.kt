package io.github.juevigrace.diva.database.test

import io.github.juevigrace.diva.database.driver.DriverProvider
import io.github.juevigrace.diva.database.sqlite.JsDriverProvider
import io.github.juevigrace.diva.database.sqlite.config.SqliteConf

actual val provider: DriverProvider<SqliteConf> = JsDriverProvider(SqliteConf("test", inMemory = true))
