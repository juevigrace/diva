package com.diva.app.file.domain.services

import com.diva.app.file.models.Directory
import com.diva.app.file.models.DirectoryListing

/**
 * Platform reader for the device filesystem. Implementations never touch the
 * database; they only translate native listings into
 * [com.diva.app.file.models.Directory]/[com.diva.app.file.models.DeviceFile]
 * values (fresh UUID ids, `parent` left unset — the repository attaches it).
 */
interface FileSystemService {

    /** Browsable roots for this platform (desktop home dir, shared storage, iOS Documents). */
    suspend fun roots(): List<Directory>

    /** Immediate children of [parent] (subdirectories and regular files). */
    suspend fun listChildren(parent: Directory): DirectoryListing
}
