package hu.simplexion.adaptive.email.table

import hu.simplexion.adaptive.email.model.Email
import hu.simplexion.adaptive.email.model.EmailStatus
import hu.simplexion.adaptive.exposed.asCommon
import hu.simplexion.adaptive.exposed.asJvm
import hu.simplexion.adaptive.exposed.jeq
import hu.simplexion.adaptive.server.components.StoreImpl
import hu.simplexion.adaptive.utility.UUID
import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.toJavaInstant
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

open class EmailTable: UUIDTable("email", columnName = "uuid"), StoreImpl<EmailTable> {

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
            .map {
                Email(
                    uuid = it[id].value.asCommon(),
                    recipients = it[recipients],
                    subject = it[subject],
                    content = it[content],
                    status = it[status],
                    createdAt = it[createdAt],
                    sentAt = it[sentAt],
                    sensitive = it[sensitive],
                    hasAttachment = it[hasAttachment],
                    contentType = it[contentType]
                )
            }
            .single()

}



