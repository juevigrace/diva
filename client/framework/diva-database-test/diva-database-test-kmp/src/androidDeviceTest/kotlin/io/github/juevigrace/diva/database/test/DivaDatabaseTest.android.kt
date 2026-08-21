package io.github.juevigrace.diva.database.test

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import io.github.juevigrace.diva.database.driver.DriverProvider
import io.github.juevigrace.diva.database.sqlite.AndroidDriverProvider
import io.github.juevigrace.diva.database.sqlite.config.SqliteConf

actual val provider: DriverProvider<SqliteConf> = AndroidDriverProvider(
    context = ApplicationProvider.getApplicationContext<Context>(),
    conf = SqliteConf("test", inMemory = true),
)