package hu.simplexion.adaptive.email.model

import hu.simplexion.adaptive.utility.UUID
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class EmailQueueEntry(
    val email: UUID<Email>,
    val tries: Int = 0,
    val lastTry: Instant? = Clock.System.now(),
    val lastFailMessage: String? = null
)