package hu.simplexion.adaptive.email.model

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.adat.AdatClass
import hu.simplexion.adaptive.utility.UUID
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Adat
class Email(
    val recipients : String,
    val subject: String,
    val content : String,
    val uuid : UUID<Email> = UUID(),
    val status : EmailStatus = EmailStatus.SendWait,
    val createdAt : Instant = Clock.System.now(),
    val sentAt : Instant? = null,
    val sensitive : Boolean = true,
    val hasAttachment : Boolean = false,
    val contentType : String = "text/plain"
) : AdatClass<Email>