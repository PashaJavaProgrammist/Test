package com.freshly.interview.presentation

import com.freshly.interview.domain.EventDomain

/**
 * Event model for presentation layer
 */
data class EventPresentation(
    val id: Long,
    val name: String,
    val url: String,
    val date: String,
    val time: String,
    val favorite: Boolean,
) {

    companion object {

        /**
         * Convert Event model for domain layer to Event model to presentation layer (for DB)
         */
        fun EventDomain.toEventPresentation(): EventPresentation {
            return EventPresentation(
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