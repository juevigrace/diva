package com.diva.models.server

import com.diva.models.auth.Session
import io.ktor.util.AttributeKey

const val SESSION_KEY_STR = "session_key"
val SESSION_KEY = AttributeKey<Session>(SESSION_KEY_STR)
