package com.diva.app.file.domain.services

/**
 * Deterministic extension-based mime guess. Platform content-resolver style
 * probes are flaky across targets, so the cache uses this common table instead.
 */
internal fun guessMimeType(fileName: String): String? {
    val ext = fileName.substringAfterLast('.', missingDelimiterValue = "").lowercase()
    if (ext.isEmpty()) return null
    return MIME_BY_EXTENSION[ext]
}

private val MIME_BY_EXTENSION: Map<String, String> = mapOf(
    // audio
    "mp3" to "audio/mpeg",
    "flac" to "audio/flac",
    "m4a" to "audio/mp4",
    "aac" to "audio/aac",
    "ogg" to "audio/ogg",
    "opus" to "audio/ogg",
    "wav" to "audio/wav",
    "aiff" to "audio/aiff",
    // video
    "mp4" to "video/mp4",
    "mkv" to "video/x-matroska",
    "webm" to "video/webm",
    "mov" to "video/quicktime",
    "avi" to "video/x-msvideo",
    // image
    "png" to "image/png",
    "jpg" to "image/jpeg",
    "jpeg" to "image/jpeg",
    "gif" to "image/gif",
    "webp" to "image/webp",
    "bmp" to "image/bmp",
    "svg" to "image/svg+xml",
    // text-ish
    "txt" to "text/plain",
    "md" to "text/markdown",
    "pdf" to "application/pdf",
    "json" to "application/json",
    "xml" to "application/xml",
    "srt" to "application/x-subrip",
    "vtt" to "text/vtt",
)
