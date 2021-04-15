package com.freshly.interview.common.time

/**
 * Simple converter which extract time from UTC date string
 */
class TimeConverter : DateTimeUtcConverter {

    override fun convert(dateTimeUtc: String): String {
        val i = dateTimeUtc.indexOf('T')
        return dateTimeUtc.substring(i + 1, dateTimeUtc.length)
    }
}