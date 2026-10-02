package io.github.juevigrace.diva.lib.ui.components.carousel

/**
 * Platform defaults for [Carousel].
 *
 * Touch platforms get the pager's own gestures and no edge arrows, since a
 * swipe is the expected affordance there. Pointer platforms get click-and-drag
 * panning and edge arrows, since the pager has no pointer drag on its own.
 */
expect object CarouselDefaults {
    /** Whether edge arrows are offered unless a caller opts out. */
    val arrowsEnabled: Boolean

    /** Whether the pager needs the extra pointer-drag handler. */
    val dragScrollEnabled: Boolean
}