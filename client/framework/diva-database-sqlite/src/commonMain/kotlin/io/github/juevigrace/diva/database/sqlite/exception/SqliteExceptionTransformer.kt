package io.github.juevigrace.diva.database.sqlite.exception

import io.github.juevigrace.diva.database.exception.DatabaseExceptionTransformer
import io.github.juevigrace.diva.database.exception.DivaDatabaseException

expect object SqliteExceptionTransformer : DatabaseExceptionTransformer {
    override fun transform(throwable: Throwable): DivaDatabaseException
}
