package io.github.juevigrace.diva.database.mysql.config

import io.github.juevigrace.diva.database.config.DriverConf

data class MysqlConf(
    val host: String = "localhost",
    val port: Int = 3306,
    val database: String,
    val username: String,
    val password: String,
    override val properties: Map<String, String> = emptyMap(),
) : DriverConf
