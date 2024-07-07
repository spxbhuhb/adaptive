package hu.simplexion.adaptive.email.model

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.adat.AdatEntity
import hu.simplexion.adaptive.utility.UUID
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Adat
class Email(
    override val id: UUID<Email> = UUID(),
    val recipients : String,
    val subject: String,
    val content : String,
    val status : EmailStatus = EmailStatus.SendWait,
    val createdAt : Instant = Clock.System.now(),
    val sentAt : Instant? = null,
    val sensitive : Boolean = true,
    val hasAttachment : Boolean = false,
    val contentType : String = "text/plain"
) : AdatEntity<Email>