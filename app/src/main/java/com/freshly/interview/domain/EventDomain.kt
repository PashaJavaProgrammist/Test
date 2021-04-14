package com.freshly.interview.domain

import com.freshly.interview.data.db.EventDbo

data class EventDomain constructor(
    val id: Long,
    val name: String,
    val url: String,
    val date: String,
    val time: String,
    val favorite: Boolean,
) {

    companion object {

        fun EventDomain.toEventDbo(): EventDbo {
            return EventDbo(
                id = this.id,
                name = this.name,
                url = this.url,
                time = this.time,
                date = this.date,
                favorite = this.favorite,
            )
        }
    }
}