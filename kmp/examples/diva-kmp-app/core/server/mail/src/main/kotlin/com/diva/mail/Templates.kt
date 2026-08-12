package com.diva.mail

import com.diva.models.server.UserVerification
import kotlinx.html.BODY
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.h2
import kotlinx.html.head
import kotlinx.html.html
import kotlinx.html.lang
import kotlinx.html.meta
import kotlinx.html.p
import kotlinx.html.stream.createHTML
import kotlinx.html.strong
import kotlinx.html.style
import kotlinx.html.unsafe
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun buildCodeVerificationEmail(verification: UserVerification): String {
    val remainingTime: Duration = Clock.System.now().let { currentTime ->
        verification.expiresAt - currentTime
    }
    val minutesRemaining: Long = remainingTime.inWholeMinutes

    return buildEmailHTML {
        div(classes = "container") {
            div(classes = "message") {
                h2 { +"Email Verification" }
                p { +"Please use the verification code below to complete your email verification:" }
            }

            div(classes = "verification-code") {
                +verification.token
            }

            div(classes = "message") {
                p { +"Copy this code and return to the application to enter it for verification." }
            }

            div(classes = "expiration") {
                p {
                    strong { +"Note:" }
                    +" This verification code will expire in "
                    strong { +"$minutesRemaining" }
                    +" minute${if (minutesRemaining != 1L) "s" else ""} for your security."
                }
            }
        }
    }
}

fun buildEmailHTML(block: BODY.() -> Unit): String {
    return createHTML().html {
        lang = "en"
        head {
            meta(charset = "utf-8")
            meta(name = "viewport", content = "width=device-width, initial-scale=1")
            style {
                unsafe {
                    """
                        body {
                            font-family: Arial, sans-serif;
                            line-height:1.6;
                            color: #333;
                            max-width: 600px;
                            margin: 0 auto;
                            padding: 20px;
                        }
                        .container {
                            background-color: #f9f9f9;
                            padding: 20px;
                            border-radius: 8px;
                        }
                        .btn {
                            display: inline-block;
                            padding: 12px 24px;
                            background-color: #007bff;
                            color: white;
                            text-decoration: none;
                            border-radius: 4px;
                        }
                        .verification-code {
                            font-size: 32px;
                            font-weight: bold;
                            text-align: center;
                            letter-spacing: 8px;
                            background-color: #e9ecef;
                            padding: 20px;
                            margin: 20px 0;
                            border-radius: 8px;
                            font-family: 'Courier New', monospace;
                        }
                        .message {
                            text-align: center;
                            margin-bottom: 20px;
                        }
                        .expiration {
                            text-align: center;
                            font-size: 14px;
                            color: #666;
                            margin-top: 30px;
                        }
                    """.trimIndent()
                }
            }
        }
        body {
            block()
        }
    }
}
