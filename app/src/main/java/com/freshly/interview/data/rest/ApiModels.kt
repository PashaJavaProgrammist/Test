package com.freshly.interview.data.rest

import com.freshly.interview.common.EMPTY
import com.freshly.interview.domain.EventDomain
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
) {

    companion object {

        fun Event.toEventDomain(
            dateConvert: (String) -> String,
            timeConvert: (String) -> String,
        ): EventDomain {
            return EventDomain(
                id = this.id ?: 0L,
                name = this.venue?.name ?: String.EMPTY,
                url = this.venue?.url ?: String.EMPTY,
                date = this.datetimeUtc?.let(dateConvert) ?: String.EMPTY,
                time = this.datetimeUtc?.let(timeConvert) ?: String.EMPTY,
                favorite = false,
            )
        }
    }
}

data class Venue(
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("url")
    var url: String? = null,
)