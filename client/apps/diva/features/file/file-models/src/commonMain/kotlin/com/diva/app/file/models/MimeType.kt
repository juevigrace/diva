@file:DivaJsExport

package com.diva.app.file.models

import io.github.juevigrace.diva.core.DivaJsExport

/**
 * Known file mime types, keyed by the extensions that map to them. Extension
 * based detection is deterministic across targets, so the cache uses this
 * closed set instead of platform content-resolver probes.
 */
enum class MimeType(val value: String, vararg val extensions: String) {
    MP3("audio/mpeg", "mp3"),
    FLAC("audio/flac", "flac"),
    M4A("audio/mp4", "m4a"),
    AAC("audio/aac", "aac"),
    OGG("audio/ogg", "ogg", "opus"),
    WAV("audio/wav", "wav"),
    AIFF("audio/aiff", "aiff"),
    MP4("video/mp4", "mp4"),
    MKV("video/x-matroska", "mkv"),
    WEBM("video/webm", "webm"),
    MOV("video/quicktime", "mov"),
    AVI("video/x-msvideo", "avi"),
    PNG("image/png", "png"),
    JPEG("image/jpeg", "jpg", "jpeg"),
    GIF("image/gif", "gif"),
    WEBP("image/webp", "webp"),
    BMP("image/bmp", "bmp"),
    SVG("image/svg+xml", "svg"),
    TXT("text/plain", "txt"),
    MARKDOWN("text/markdown", "md"),
    PDF("application/pdf", "pdf"),
    JSON("application/json", "json"),
    XML("application/xml", "xml"),
    SRT("application/x-subrip", "srt"),
    VTT("text/vtt", "vtt"),
    ;

    companion object {
        private val BY_EXTENSION: Map<String, MimeType> =
            entries.flatMap { mime -> mime.extensions.map { extension -> extension to mime } }.toMap()

        fun fromExtension(extension: String): MimeType? = BY_EXTENSION[extension.lowercase()]

        fun fromFileName(fileName: String): MimeType? {
            val extension = fileName.substringAfterLast('.', missingDelimiterValue = "")
            if (extension.isEmpty()) return null
            return BY_EXTENSION[extension.lowercase()]
        }
    }
}
