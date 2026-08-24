package io.github.juevigrace.diva.network.client

import io.ktor.http.ContentType

sealed interface FormDataContent {
    data class FormItem(val key: String, val value: String) : FormDataContent

    data class FileItem(
        val key: String,
        val bytes: ByteArray,
        val fileName: String,
        val contentType: ContentType,
    ) : FormDataContent {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is FileItem) return false
            return key == other.key &&
                bytes.contentEquals(other.bytes) &&
                fileName == other.fileName &&
                contentType == other.contentType
        }

        override fun hashCode(): Int {
            var result = key.hashCode()
            result = 31 * result + bytes.contentHashCode()
            result = 31 * result + fileName.hashCode()
            result = 31 * result + contentType.hashCode()
            return result
        }
    }
}
