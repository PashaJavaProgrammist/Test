package com.freshly.interview.domain

import com.freshly.interview.data.db.EventDbo

/**
 * Event model for domain layer
 */
data class EventDomain constructor(
    val id: Long,
    val name: String,
    val url: String,
    val date: String,
    val time: String,
    var favorite: Boolean,
) {

    companion object {

        /**
         * Convert Event model for domain layer to Event model to data layer (for DB)
         */
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