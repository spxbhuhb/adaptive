package hu.simplexion.adaptive.email.store

import hu.simplexion.adaptive.email.model.Email
import hu.simplexion.adaptive.email.model.EmailStatus
import hu.simplexion.adaptive.exposed.AdatEntityTable
import hu.simplexion.adaptive.exposed.uuidEq
import hu.simplexion.adaptive.utility.UUID
import kotlinx.datetime.Clock.System.now
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.update

object EmailTable : AdatEntityTable<Email, EmailTable>("email") {

    val recipients = text("recipients")
    val subject = text("subject")
    val content = text("content")

    val status = enumerationByName<EmailStatus>("status", 20)

    val createdAt = timestamp("createdAt")
    val sentAt = timestamp("sentAt").nullable()
    val sensitive = bool("sensitive")
    val hasAttachment = bool("hasAttachment")

    val contentType = varchar("contentType", 60)

    fun setStatus(uuid: UUID<Email>, inStatus: EmailStatus) {
        update({ id uuidEq uuid }) {
            it[status] = inStatus
            if (inStatus == EmailStatus.Sent) {
                it[sentAt] = now()
            }
        }
    }

}



