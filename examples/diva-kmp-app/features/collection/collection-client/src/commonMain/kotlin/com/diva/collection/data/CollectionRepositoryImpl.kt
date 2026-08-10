package com.diva.collection.data

import com.diva.collection.api.client.CollectionClient
import com.diva.database.collection.CollectionStorage

class CollectionRepositoryImpl(
    private val client: CollectionClient,
    private val storage: CollectionStorage,
) : CollectionRepository
