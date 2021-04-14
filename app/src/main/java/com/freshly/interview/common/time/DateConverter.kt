package com.freshly.interview.common.time

/**
 * Simple converter which extract date from UTC date string
 */
class DateConverter : DateTimeUtcConverter {

    override fun convert(dateTimeUtc: String): String {
        val i = dateTimeUtc.indexOf('T')
        return dateTimeUtc.substring(0, i)
    }
}