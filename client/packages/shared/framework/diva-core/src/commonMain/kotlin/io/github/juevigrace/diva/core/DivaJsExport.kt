package io.github.juevigrace.diva.core

import kotlin.js.ExperimentalJsExport

/**
 * Annotation to export declarations to JavaScript.
 *
 * This is a wrapper around [kotlin.js.JsExport] that is safely ignored by other platforms,
 * including WasmJS where declaration-level [kotlin.js.JsExport] is restricted to functions.
 */
@OptIn(ExperimentalMultiplatform::class, ExperimentalJsExport::class)
@OptionalExpectation
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.FILE,
)
expect annotation class DivaJsExport()
