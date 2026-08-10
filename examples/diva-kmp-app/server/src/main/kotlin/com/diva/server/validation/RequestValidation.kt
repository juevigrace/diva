package com.diva.server.validation

import com.diva.auth.api.validation.authRequestValidation
import com.diva.chat.api.validation.chatRequestValidation
import com.diva.collection.api.validation.collectionRequestValidation
import com.diva.media.api.validation.mediaRequestValidation
import com.diva.permissions.api.validation.permissionsRequestValidation
import com.diva.social.api.validation.socialRequestValidation
import com.diva.user.api.validation.userRequestValidation
import com.diva.verification.api.validation.verificationRequestValidation
import io.ktor.server.plugins.requestvalidation.RequestValidationConfig

fun RequestValidationConfig.appRequestValidation() {
    authRequestValidation()
    chatRequestValidation()
    collectionRequestValidation()
    mediaRequestValidation()
    permissionsRequestValidation()
    socialRequestValidation()
    userRequestValidation()
    verificationRequestValidation()
}
