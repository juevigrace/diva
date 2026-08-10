package com.diva.permissions.data

import com.diva.database.permissions.PermissionsStorage

class PermissionsRepositoryImpl(
    private val storage: PermissionsStorage,
) : PermissionsRepository
