package `fun`.adaptive.email.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatEntity
import `fun`.adaptive.utility.UUID
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