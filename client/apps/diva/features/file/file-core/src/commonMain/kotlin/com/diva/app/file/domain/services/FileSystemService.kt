package com.diva.app.file.domain.services

import com.diva.app.file.models.Directory

interface FileSystemService {
    suspend fun roots(): Result<List<Directory>>

    /**
     * Reads the directory at [path] (which must be one of the roots or a
     * descendant of one of them) and returns it with its immediate children in
     * [Directory.entries]. Never recursive. Fails when [path] escapes every root
     * or cannot be read.
     */
    suspend fun read(path: String): Result<Directory>
}
