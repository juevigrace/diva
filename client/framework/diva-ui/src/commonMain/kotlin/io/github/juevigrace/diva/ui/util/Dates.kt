package io.github.juevigrace.diva.ui.util

import androidx.compose.material3.SelectableDates
import io.github.juevigrace.diva.core.dates.DateFilter
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

// DatePicker hands out UTC-encoded calendar dates; decode strictly in UTC,
// while DateFilter decides "today" in whichever TimeZone it was built with.
fun DateFilter.toSelectableDates(): SelectableDates {
    val filter = this
    return object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            val date = Instant.fromEpochMilliseconds(utcTimeMillis)
                .toLocalDateTime(TimeZone.UTC)
                .date
            return filter.isSelectable(date)
        }

        override fun isSelectableYear(year: Int): Boolean {
            return filter.isSelectableYear(year)
        }
    }
}
