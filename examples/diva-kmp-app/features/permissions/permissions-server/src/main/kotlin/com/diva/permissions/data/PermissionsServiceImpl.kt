package com.diva.permissions.data

import com.diva.database.permissions.PermissionsStorage

class PermissionsServiceImpl(
    private val storage: PermissionsStorage,
) : PermissionsService
