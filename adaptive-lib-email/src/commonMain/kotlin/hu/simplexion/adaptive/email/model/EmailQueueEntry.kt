package hu.simplexion.adaptive.email.model

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.adat.AdatClass
import hu.simplexion.adaptive.utility.UUID
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