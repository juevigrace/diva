package io.github.juevigrace.diva.database.postgres.config

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.database.config.DriverConf

data class PostgresConf(
    val host: String = "localhost",
    val port: Int = 5432,
    val database: String,
    val username: String,
    val password: String,
    val schema: Option<String> = Option.None,
    override val properties: Map<String, String> = emptyMap(),
) : DriverConf
