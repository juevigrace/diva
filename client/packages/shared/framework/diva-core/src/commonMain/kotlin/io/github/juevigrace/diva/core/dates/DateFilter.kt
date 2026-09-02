package io.github.juevigrace.diva.core.dates

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock

fun interface DateFilter {
    fun isSelectable(date: LocalDate): Boolean

    fun isSelectableYear(year: Int): Boolean = true
}

object DateFilters {

    val any: DateFilter = object : DateFilter {
        override fun isSelectable(date: LocalDate): Boolean = true

        override fun isSelectableYear(year: Int): Boolean = true
    }

    fun from(min: LocalDate): DateFilter = object : DateFilter {
        override fun isSelectable(date: LocalDate): Boolean = date >= min

        override fun isSelectableYear(year: Int): Boolean = year >= min.year
    }

    fun to(max: LocalDate): DateFilter = object : DateFilter {
        override fun isSelectable(date: LocalDate): Boolean = date <= max

        override fun isSelectableYear(year: Int): Boolean = year <= max.year
    }

    fun between(min: LocalDate, max: LocalDate): DateFilter {
        require(min <= max) { "min ($min) must not be after max ($max)" }
        return object : DateFilter {
            override fun isSelectable(date: LocalDate): Boolean = date in min..max

            override fun isSelectableYear(year: Int): Boolean = year in min.year..max.year
        }
    }

    fun fromToday(timeZone: TimeZone, clock: Clock = Clock.System): DateFilter =
        from(clock.todayIn(timeZone))

    fun untilToday(timeZone: TimeZone, clock: Clock = Clock.System): DateFilter =
        to(clock.todayIn(timeZone))
}

infix fun DateFilter.and(other: DateFilter): DateFilter = object : DateFilter {
    override fun isSelectable(date: LocalDate): Boolean =
        this@and.isSelectable(date) && other.isSelectable(date)

    override fun isSelectableYear(year: Int): Boolean =
        this@and.isSelectableYear(year) && other.isSelectableYear(year)
}

infix fun DateFilter.or(other: DateFilter): DateFilter = object : DateFilter {
    override fun isSelectable(date: LocalDate): Boolean =
        this@or.isSelectable(date) || other.isSelectable(date)

    override fun isSelectableYear(year: Int): Boolean =
        this@or.isSelectableYear(year) || other.isSelectableYear(year)
}

operator fun DateFilter.not(): DateFilter = object : DateFilter {
    override fun isSelectable(date: LocalDate): Boolean = !this@not.isSelectable(date)

    override fun isSelectableYear(year: Int): Boolean = !this@not.isSelectableYear(year)
}
