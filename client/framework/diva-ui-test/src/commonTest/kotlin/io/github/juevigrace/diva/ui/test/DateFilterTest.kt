package io.github.juevigrace.diva.ui.test

import io.github.juevigrace.diva.core.dates.DateFilters
import io.github.juevigrace.diva.core.dates.and
import io.github.juevigrace.diva.core.dates.not
import io.github.juevigrace.diva.core.dates.or
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlin.test.Test
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class DateFilterTest {

    private val today = LocalDate(2026, 8, 25)
    private val utc = TimeZone.UTC

    private val fixedClock = object : Clock {
        override fun now(): Instant = Instant.parse("2026-08-25T12:00:00Z")
    }

    @Test
    fun anyAcceptsEverything() {
        assertTrue(DateFilters.any.isSelectable(LocalDate(1990, 1, 1)))
        assertTrue(DateFilters.any.isSelectableYear(3000))
    }

    @Test
    fun fromIsInclusive() {
        val filter = DateFilters.from(today)
        assertTrue(filter.isSelectable(today))
        assertTrue(filter.isSelectable(today.plus(1, DateTimeUnit.DAY)))
        assertFalse(filter.isSelectable(today.minus(1, DateTimeUnit.DAY)))
    }

    @Test
    fun fromYearBound() {
        val filter = DateFilters.from(today)
        assertFalse(filter.isSelectableYear(2025))
        assertTrue(filter.isSelectableYear(2026))
        assertTrue(filter.isSelectableYear(2027))
    }

    @Test
    fun toIsInclusive() {
        val filter = DateFilters.to(today)
        assertTrue(filter.isSelectable(today))
        assertFalse(filter.isSelectable(today.plus(1, DateTimeUnit.DAY)))
        assertTrue(filter.isSelectable(today.minus(1, DateTimeUnit.DAY)))
    }

    @Test
    fun betweenBoundsBothSides() {
        val min = LocalDate(2026, 1, 1)
        val max = LocalDate(2026, 12, 31)
        val filter = DateFilters.between(min, max)
        assertTrue(filter.isSelectable(min))
        assertTrue(filter.isSelectable(max))
        assertFalse(filter.isSelectable(min.minus(1, DateTimeUnit.DAY)))
        assertFalse(filter.isSelectable(max.plus(1, DateTimeUnit.DAY)))
    }

    @Test
    fun betweenRejectsInvertedRange() {
        assertFailsWith<IllegalArgumentException> {
            DateFilters.between(LocalDate(2026, 12, 31), LocalDate(2026, 1, 1))
        }
    }

    @Test
    fun fromTodayUsesProvidedClockAndZone() {
        val filter = DateFilters.fromToday(utc, fixedClock)
        assertTrue(filter.isSelectable(today))
        assertFalse(filter.isSelectable(LocalDate(2026, 8, 24)))
    }

    @Test
    fun untilTodayUsesProvidedClockAndZone() {
        val filter = DateFilters.untilToday(utc, fixedClock)
        assertTrue(filter.isSelectable(today))
        assertFalse(filter.isSelectable(LocalDate(2026, 8, 26)))
    }

    @Test
    fun combinatorsCompose() {
        val window = DateFilters.between(LocalDate(2026, 8, 20), LocalDate(2026, 8, 30))
        val notToday = !DateFilters.fromToday(utc, fixedClock)
        val combined = window and notToday

        assertTrue(combined.isSelectable(LocalDate(2026, 8, 24)))
        assertFalse(combined.isSelectable(today))
        assertFalse(combined.isSelectable(LocalDate(2026, 9, 1)))

        val either = DateFilters.from(today) or DateFilters.to(LocalDate(2020, 1, 1))
        assertTrue(either.isSelectable(LocalDate(2019, 1, 1)))
        assertTrue(either.isSelectable(LocalDate(2027, 1, 1)))
        assertFalse(either.isSelectable(LocalDate(2023, 1, 1)))
    }

    @Test
    fun yearFilterDerivedFromBounds() {
        val filter = DateFilters.between(LocalDate(2026, 8, 20), LocalDate(2026, 8, 30))
        assertTrue(filter.isSelectableYear(2026))
        assertFalse(filter.isSelectableYear(2025))
        assertFalse(filter.isSelectableYear(2027))
    }
}
