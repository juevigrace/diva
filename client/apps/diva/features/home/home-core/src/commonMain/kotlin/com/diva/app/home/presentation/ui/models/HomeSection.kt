package com.diva.app.home.presentation.ui.models

import com.diva.app.media.models.Media

data class HomeSection(
    val id: String,
    val title: String,
    val media: List<Media>,
)
