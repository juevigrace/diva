package com.diva.app.database.di

import com.diva.app.core.AppDatabase
import com.diva.app.database.DivaDB
import com.diva.app.database.appDivaDBMapper
import io.github.juevigrace.diva.database.DivaDatabase
import io.github.juevigrace.diva.database.driver.DriverProvider
import io.github.juevigrace.diva.database.sqlite.config.SqliteConf
import io.github.juevigrace.diva.lib.core.SharedDatabase
import io.github.juevigrace.diva.lib.database.DivaSharedDB
import io.github.juevigrace.diva.lib.database.sharedDBMapper
import kotlinx.coroutines.runBlocking
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

fun databaseModule(): Module {
    return module {
        includes(platformDriverModule())

        single<DivaDatabase<DivaSharedDB>>(qualifier = SharedDatabase) {
            runBlocking {
                DivaDatabase.createAsync(
                    provider = get<DriverProvider<SqliteConf>> { parametersOf(SqliteConf(name = "diva_shared.db")) },
                    schema = DivaSharedDB.Schema,
                    db = ::sharedDBMapper,
                ).getOrThrow()
            }
        }

        single<DivaDatabase<DivaDB>>(qualifier = AppDatabase) {
            runBlocking {
                DivaDatabase.createAsync(
                    provider = get<DriverProvider<SqliteConf>> { parametersOf(SqliteConf(name = "diva_app.db")) },
                    schema = DivaDB.Schema,
                    db = ::appDivaDBMapper,
                ).getOrThrow()
            }
        }
    }
}
