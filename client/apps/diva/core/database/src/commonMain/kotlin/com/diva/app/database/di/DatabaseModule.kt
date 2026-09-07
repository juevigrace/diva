package com.diva.app.database.di

import com.diva.app.database.DivaDB
import com.diva.app.database.appDivaDBMapper
import com.diva.app.database.sharedDBMapper
import io.github.juevigrace.diva.database.DivaDatabase
import io.github.juevigrace.diva.database.sqlite.config.SqliteConf
import io.github.juevigrace.diva.lib.database.DivaSharedDB
import kotlinx.coroutines.runBlocking
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

fun databaseModule(): Module {
    return module {
        includes(platformDriverModule())

        single<DivaDatabase<DivaSharedDB>> {
            runBlocking {
                DivaDatabase.createAsync(
                    provider = get<SqliteDriverProvider> { parametersOf(SqliteConf(name = "diva.db")) },
                    schema = DivaSharedDB.Schema,
                    db = ::sharedDBMapper,
                ).getOrThrow()
            }
        }

        single<DivaDatabase<DivaDB>> {
            runBlocking {
                DivaDatabase.createAsync(
                    provider = get<SqliteDriverProvider> { parametersOf(SqliteConf(name = "diva_app.db")) },
                    schema = DivaDB.Schema,
                    db = ::appDivaDBMapper,
                ).getOrThrow()
            }
        }
    }
}
