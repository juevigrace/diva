package io.github.juevigrace.diva.database.exception

interface DatabaseExceptionTransformer {
    fun transform(throwable: Throwable): DivaDatabaseException
}
