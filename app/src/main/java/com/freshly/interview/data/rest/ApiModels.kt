package com.freshly.interview.data.rest

import com.google.gson.annotations.SerializedName

data class Events(
    @SerializedName("events")
    var events: List<Event>? = null
)

data class Event(
    @SerializedName("id")
    var id: Long? = null,
    @SerializedName("datetime_utc")
    var datetimeUtc: String? = null,
    @SerializedName("venue")
    var venue: Venue? = null
)

data class Venue(
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("url")
    var url: String? = null,
)