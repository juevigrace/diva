package com.diva.mail

import com.resend.Resend
import com.resend.services.emails.model.CreateEmailOptions
import io.github.juevigrace.diva.core.DivaResult
import io.github.juevigrace.diva.core.errors.DivaError
import io.github.juevigrace.diva.core.errors.toDivaError
import io.github.juevigrace.diva.core.tryResult
import kotlinx.html.BODY

class KMail(
    private val config: KMailConfig,
) {
    private val resend = Resend(config.apiKey)

    fun sendEmail(to: String, subject: String, html: String): DivaResult<Unit, DivaError> {
        return tryResult(
            onError = { e -> e.toDivaError() }
        ) {
            val request = CreateEmailOptions.builder()
                .from(config.fromEmail)
                .to(listOf(to))
                .subject(subject)
                .html(html)
                .build()

            resend.emails().send(request)
            DivaResult.success(Unit)
        }
    }

    fun sendEmail(to: String, subject: String, block: BODY.() -> Unit): DivaResult<Unit, DivaError> {
        val html: String = buildEmailHTML(block)
        return sendEmail(to, subject, html)
    }
}
