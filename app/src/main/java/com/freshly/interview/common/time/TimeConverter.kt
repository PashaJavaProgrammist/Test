package com.freshly.interview.common.time

class TimeConverter : DateTimeUtcConverter {

    override fun convert(dateTimeUtc: String): String {
        val i = dateTimeUtc.indexOf('T')
        return dateTimeUtc.substring(i + 1, dateTimeUtc.length - 1)
    }
}