package com.freshly.interview.presentation

import com.freshly.interview.domain.EventDomain

data class EventPresentation(
    val name: String,
    val url: String,
    val date: String,
    val time: String,
) {

    companion object {

        fun EventDomain.toEventPresentation(): EventPresentation {
            return EventPresentation(
                name = this.name,
                url = this.url,
                time = this.time,
                date = this.date,
            )
        }
    }
}