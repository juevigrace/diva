package com.diva.app.file.models

/** The immediate children of one directory as seen on the device filesystem. */
data class DirectoryListing(
    val directories: List<Directory>,
    val files: List<DeviceFile>,
)
