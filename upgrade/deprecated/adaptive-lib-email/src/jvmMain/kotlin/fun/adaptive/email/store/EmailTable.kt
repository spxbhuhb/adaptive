package `fun`.adaptive.email.store

import `fun`.adaptive.email.model.Email
import `fun`.adaptive.email.model.EmailStatus
import `fun`.adaptive.exposed.AdatEntityTable
import `fun`.adaptive.exposed.ExposedAdatTable
import `fun`.adaptive.exposed.uuidEq
import `fun`.adaptive.utility.UUID
import kotlin.time.Clock.System.now
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.update

@ExposedAdatTable
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



