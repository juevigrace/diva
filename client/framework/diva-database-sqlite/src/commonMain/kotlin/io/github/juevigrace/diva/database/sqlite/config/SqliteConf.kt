package io.github.juevigrace.diva.database.sqlite.config

import io.github.juevigrace.diva.database.config.DriverConf

data class SqliteConf(
    val name: String,
    val inMemory: Boolean = false,
    override val properties: Map<String, String> = emptyMap(),
) : DriverConf