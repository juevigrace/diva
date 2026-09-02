package io.github.juevigrace.diva.database.exception

sealed class DivaDatabaseException(
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)

class UniqueConstraintViolation(
    message: String,
    cause: Throwable? = null
) : DivaDatabaseException(message, cause)

class ForeignKeyConstraintViolation(
    message: String,
    cause: Throwable? = null
) : DivaDatabaseException(message, cause)

class NotNullConstraintViolation(
    message: String,
    cause: Throwable? = null
) : DivaDatabaseException(message, cause)

class CheckConstraintViolation(
    message: String,
    cause: Throwable? = null
) : DivaDatabaseException(message, cause)

class DatabaseLockedException(
    message: String,
    cause: Throwable? = null
) : DivaDatabaseException(message, cause)

class DatabaseCorruptionException(
    message: String,
    cause: Throwable? = null
) : DivaDatabaseException(message, cause)

class DiskFullException(
    message: String,
    cause: Throwable? = null
) : DivaDatabaseException(message, cause)

class DatabaseConnectionException(
    message: String,
    cause: Throwable? = null
) : DivaDatabaseException(message, cause)

class DatabaseSchemaException(
    message: String,
    cause: Throwable? = null
) : DivaDatabaseException(message, cause)

class DatabaseQueryException(
    message: String,
    cause: Throwable? = null
) : DivaDatabaseException(message, cause)

class DatabaseIOIncomingError(
    message: String,
    cause: Throwable? = null
) : DivaDatabaseException(message, cause)

class UnknownDatabaseException(
    message: String,
    cause: Throwable? = null
) : DivaDatabaseException(message, cause)
