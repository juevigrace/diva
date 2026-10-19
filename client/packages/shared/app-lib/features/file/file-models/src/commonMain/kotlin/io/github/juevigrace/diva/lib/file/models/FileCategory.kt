@file:DivaJsExport

package io.github.juevigrace.diva.lib.file.models

import io.github.juevigrace.diva.core.DivaJsExport

/**
 * Coarse file classification derived from [MimeType] at scan time, used to
 * filter listings in the cache queries (e.g. media-only browsing).
 */
enum class FileCategory {
    AUDIO,
    IMAGE,
    VIDEO,
    DOCUMENT,
    OTHER,
    ;

    companion object {
        fun fromMimeType(mimeType: MimeType?): FileCategory {
            if (mimeType == null) return OTHER
            return when {
                mimeType.value.startsWith("audio/") -> AUDIO
                mimeType.value.startsWith("image/") -> IMAGE
                mimeType.value.startsWith("video/") -> VIDEO
                mimeType in DOCUMENT_MIMES -> DOCUMENT
                else -> OTHER
            }
        }

        private val DOCUMENT_MIMES: Set<MimeType> = setOf(
            MimeType.PDF,
            MimeType.TXT,
            MimeType.MARKDOWN,
            MimeType.JSON,
            MimeType.XML,
            MimeType.SRT,
            MimeType.VTT,
        )
    }
}
