package com.diva.app.models.collection.mix

import com.diva.app.models.collection.Collection
import io.github.juevigrace.diva.core.None
import io.github.juevigrace.diva.core.Option

data class Mix(
    val collection: Collection,
    val metadata: Option<MixMetadata> = None,
)
