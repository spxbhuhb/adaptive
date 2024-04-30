package hu.simplexion.adaptive.email.table

import hu.simplexion.adaptive.email.model.Email
import hu.simplexion.adaptive.email.model.EmailStatus
import hu.simplexion.adaptive.exposed.ExposedStoreImpl
import hu.simplexion.adaptive.exposed.asCommon
import hu.simplexion.adaptive.exposed.asJvm
import hu.simplexion.adaptive.exposed.jeq
import hu.simplexion.adaptive.server.components.StoreImpl
import hu.simplexion.adaptive.utility.UUID
import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.toJavaInstant
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

open class EmailTable: UUIDTable("email", columnName = "uuid"), ExposedStoreImpl<EmailTable> {

    val recipients = text("recipients")
    val subject = text("subject")
    val content = text("content")

    val status = enumerationByName<EmailStatus>("status", 20)

    val createdAt = timestamp("createdAt")
    val sentAt = timestamp("sentAt").nullable()
    val sensitive = bool("sensitive")
    val hasAttachment = bool("hasAttachment")

    val contentType = varchar("contentType", 60)

    fun insert(email: Email) {
        insert {
            it[id] = email.uuid.asJvm()
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

    fun setStatus(uuid: UUID<Email>, inStatus: EmailStatus) {
        update({ id jeq uuid }) {
            it[status] = inStatus
            if (inStatus == EmailStatus.Sent) {
                it[sentAt] = now()
            }
        }
    }

    operator fun get(uuid: UUID<Email>) : Email =
        select { id jeq uuid }
            .single()
            .toEmail()

    fun all() : List<Email> =
        selectAll()
            .map { it.toEmail() }

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
}



