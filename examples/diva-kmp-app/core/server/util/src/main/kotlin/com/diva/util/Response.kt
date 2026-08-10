package com.diva.util

import com.diva.models.api.ApiResponse
import io.github.juevigrace.diva.core.DivaResult
import io.github.juevigrace.diva.core.errors.DivaError
import io.github.juevigrace.diva.core.errors.toHttpStatusCodes
import io.github.juevigrace.diva.core.fold
import io.github.juevigrace.diva.core.network.HttpStatusCodes
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond

suspend inline fun<reified T> ApplicationCall.sendResponse(res: ApiResponse<T>) {
    respond(HttpStatusCode.fromValue(res.statusCode), res)
}

suspend inline fun <reified T> DivaResult<ApiResponse<T>, DivaError>.respond(call: ApplicationCall) {
    fold(
        onSuccess = { res -> call.sendResponse(res) },
        onFailure = { err ->
            call.sendResponse(
                ApiResponse<Unit>(
                    statusCode = err.cause.toHttpStatusCodes().code,
                    message = err.message
                )
            )
        }
    )
}

suspend fun ApplicationCall.respondBadRequest(message: String) {
    sendResponse(
        ApiResponse<Unit>(
            statusCode = HttpStatusCodes.BadRequest.code,
            message = message
        )
    )
}

suspend inline fun ApplicationCall.respondUnauthorized() {
    sendResponse(
        ApiResponse<Unit>(
            statusCode = HttpStatusCodes.Unauthorized.code,
            message = "authenticate first"
        )
    )
}

suspend inline fun ApplicationCall.respondNotFound(message: String) {
    sendResponse(
        ApiResponse<Unit>(
            statusCode = HttpStatusCodes.NotFound.code,
            message = message
        )
    )
}

suspend inline fun ApplicationCall.respondInternalServerError(message: String) {
    sendResponse(
        ApiResponse<Unit>(
            statusCode = HttpStatusCodes.InternalServerError.code,
            message = message
        )
    )
}
