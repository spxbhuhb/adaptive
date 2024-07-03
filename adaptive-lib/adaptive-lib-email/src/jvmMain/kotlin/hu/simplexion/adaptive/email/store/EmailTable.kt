package hu.simplexion.adaptive.email.store

import hu.simplexion.adaptive.email.model.Email
import hu.simplexion.adaptive.email.model.EmailStatus
import hu.simplexion.adaptive.exposed.ExposedStoreImpl
import hu.simplexion.adaptive.exposed.asCommon
import hu.simplexion.adaptive.exposed.asJava
import hu.simplexion.adaptive.exposed.uuidEq
import hu.simplexion.adaptive.utility.UUID
import kotlinx.datetime.Clock.System.now
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

class EmailTable : UUIDTable("email", columnName = "uuid"), ExposedStoreImpl<Email, EmailTable> {

    val recipients = text("recipients")
    val subject = text("subject")
    val content = text("content")

    val status = enumerationByName<EmailStatus>("status", 20)

    val createdAt = timestamp("createdAt")
    val sentAt = timestamp("sentAt").nullable()
    val sensitive = bool("sensitive")
    val hasAttachment = bool("hasAttachment")

    val contentType = varchar("contentType", 60)

    fun ResultRow.toEmail(): Email =
        Email(
            uuid = this[id].value.asCommon(),
            recipients = this[recipients],
            subject = this[subject],
            content = this[content],
            status = this[status],
            createdAt = this[createdAt],
            sentAt = this[sentAt],
            sensitive = this[sensitive],
            hasAttachment = this[hasAttachment],
            contentType = this[contentType]
        )

    fun all(): List<Email> =
        selectAll()
            .map { it.toEmail() }

    fun add(email: Email) {
        insert {
            it[id] = email.uuid.asJava()
            it[recipients] = email.recipients
            it[subject] = email.subject
            it[content] = email.content
            it[status] = email.status
            it[createdAt] = email.createdAt
            it[sentAt] = email.sentAt
            it[sensitive] = email.sensitive
            it[hasAttachment] = email.hasAttachment
            it[contentType] = email.contentType
        }
    }

    operator fun get(uuid: UUID<Email>): Email =
        select { id uuidEq uuid }
            .single()
            .toEmail()

    fun setStatus(uuid: UUID<Email>, inStatus: EmailStatus) {
        update({ id uuidEq uuid }) {
            it[status] = inStatus
            if (inStatus == EmailStatus.Sent) {
                it[sentAt] = now()
            }
        }
    }

}



