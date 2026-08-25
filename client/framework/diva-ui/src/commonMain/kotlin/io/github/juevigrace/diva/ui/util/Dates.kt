package io.github.juevigrace.diva.ui.util

import androidx.compose.material3.SelectableDates
import io.github.juevigrace.diva.core.dates.DateFilter
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

// DatePicker hands out UTC-encoded calendar dates; decode strictly in UTC,
// while DateFilter decides "today" in whichever TimeZone it was built with.
fun DateFilter.toSelectableDates(): SelectableDates = object : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        val date = Instant.fromEpochMilliseconds(utcTimeMillis)
            .toLocalDateTime(TimeZone.UTC)
            .date
        return isSelectable(date)
    }

    override fun isSelectableYear(year: Int): Boolean {
        return isSelectableYear(year)
    }
}
