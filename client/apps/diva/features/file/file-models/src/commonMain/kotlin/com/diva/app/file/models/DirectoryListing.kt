package com.diva.app.file.models

/** The immediate children of one directory as seen on the device filesystem. */
data class DirectoryListing(
    val entries: List<FsEntry> = emptyList(),
) {
    val dirs: List<Directory> get() = entries.filterIsInstance<Directory>()
    val files: List<DeviceFile> get() = entries.filterIsInstance<DeviceFile>()
    val isEmpty: Boolean get() = entries.isEmpty()
}
