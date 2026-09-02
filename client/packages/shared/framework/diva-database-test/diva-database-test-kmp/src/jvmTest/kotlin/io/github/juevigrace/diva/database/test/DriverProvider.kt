package io.github.juevigrace.diva.database.test

import io.github.juevigrace.diva.database.driver.DriverProvider
import io.github.juevigrace.diva.database.sqlite.JvmSqliteDriverProvider
import io.github.juevigrace.diva.database.sqlite.config.SqliteConf

actual val provider: DriverProvider<SqliteConf> = JvmSqliteDriverProvider(SqliteConf("test", inMemory = true))
