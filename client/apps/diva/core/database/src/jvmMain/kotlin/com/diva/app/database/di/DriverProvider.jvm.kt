package com.diva.app.database.di

import io.github.juevigrace.diva.database.sqlite.JvmSqliteDriverProvider
import io.github.juevigrace.diva.database.sqlite.config.SqliteConf
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformDriverModule(): Module = module {
    factory<SqliteDriverProvider> { (conf: SqliteConf) ->
        JvmSqliteDriverProvider(conf)
    }
}
