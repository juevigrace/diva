package io.github.juevigrace.diva.core.validation

// TODO: make an actual validator
interface Validator<F, T : ValidationResult> {
    fun validate(form: F): T
}

interface ValidationResult {
    val hasErrors: Boolean
    fun valid(): Boolean
}
