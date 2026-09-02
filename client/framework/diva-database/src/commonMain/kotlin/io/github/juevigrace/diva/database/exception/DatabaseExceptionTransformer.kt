package io.github.juevigrace.diva.database.exception

interface DatabaseExceptionTransformer {
    fun transform(throwable: Throwable): DivaDatabaseException
}

object DefaultDatabaseExceptionTransformer : DatabaseExceptionTransformer {
    override fun transform(throwable: Throwable): DivaDatabaseException {
        return when (throwable) {
            is DivaDatabaseException -> throwable
            else -> UnknownDatabaseException(throwable.message ?: "Unknown database error", throwable)
        }
    }
}
