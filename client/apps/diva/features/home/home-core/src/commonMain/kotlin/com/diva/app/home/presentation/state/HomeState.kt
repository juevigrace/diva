package com.diva.app.home.presentation.state

import com.diva.app.media.models.Media

data class HomeSection(
    val id: String,
    val title: String,
    val media: List<Media>,
)

data class HomeState(
    val title: String = "Diva",
    val greeting: String = "Good evening",
    val sections: List<HomeSection> = emptyList(),
)