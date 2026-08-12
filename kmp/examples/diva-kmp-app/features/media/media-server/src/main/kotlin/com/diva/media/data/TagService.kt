package com.diva.media.data

import com.diva.database.media.tag.TagStorage

interface TagService


class TagServiceImpl(
    private val storage: TagStorage,
) : TagService