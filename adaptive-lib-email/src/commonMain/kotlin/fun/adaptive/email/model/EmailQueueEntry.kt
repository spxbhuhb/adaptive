package `fun`.adaptive.email.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.utility.UUID
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Adat
class EmailQueueEntry(
    val email: UUID<Email>,
    val createdAt: Instant = Clock.System.now(),
    val tries: Int = 0,
    val lastTry: Instant? = Clock.System.now(),
    val lastFailMessage: String? = null
) : AdatClass<EmailQueueEntry>