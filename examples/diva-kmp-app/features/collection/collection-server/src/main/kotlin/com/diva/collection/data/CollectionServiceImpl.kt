package com.diva.collection.data

import com.diva.database.collection.CollectionStorage

class CollectionServiceImpl(
    private val storage: CollectionStorage,
) : CollectionService