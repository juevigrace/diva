package io.github.juevigrace.diva.lib.models.validation

object EmailValidation {
    // todo: not use this
    private val emailRegex = Regex(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
    )

    fun isValid(email: String): Boolean {
        return emailRegex.matches(email)
    }
}
