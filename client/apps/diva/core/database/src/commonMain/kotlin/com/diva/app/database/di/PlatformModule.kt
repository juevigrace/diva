package com.diva.app.database.di

import io.github.juevigrace.diva.database.driver.DriverProvider
import io.github.juevigrace.diva.database.sqlite.config.SqliteConf
import org.koin.core.module.Module

expect fun platformDriverModule(): Module

typealias SqliteDriverProvider = DriverProvider<SqliteConf>
