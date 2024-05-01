package hu.simplexion.adaptive.email.store

import hu.simplexion.adaptive.email.model.Email
import hu.simplexion.adaptive.email.model.EmailQueueEntry
import hu.simplexion.adaptive.exposed.ExposedStoreImpl
import hu.simplexion.adaptive.exposed.asCommon
import hu.simplexion.adaptive.exposed.asJvm
import hu.simplexion.adaptive.exposed.jeq
import hu.simplexion.adaptive.utility.UUID
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

open class EmailQueue : Table("email_queue"), ExposedStoreImpl<EmailQueue> {

    val email = reference("email", EmailTable())
    val createdAt = timestamp("createdAt")
    val tries = integer("tries")
    val lastTry = timestamp("lastTry").nullable()
    val lastFailMessage = text("lastFailMessage").nullable()

    fun insert(entry: EmailQueueEntry) {
        insert {
            it[email] = entry.email.asJvm()
            it[createdAt] = Clock.System.now()
            it[tries] = entry.tries
            it[lastTry] = entry.lastTry
            it[lastFailMessage] = entry.lastFailMessage
        }
    }

    fun update(entry: EmailQueueEntry) {
        update({ email jeq entry.email }) {
            it[tries] = entry.tries
            it[lastTry] = entry.lastTry
            it[lastFailMessage] = entry.lastFailMessage
        }
    }

    fun remove(uuid: UUID<Email>) {
        deleteWhere { email jeq uuid }
    }

    fun size(): Long =
        selectAll().count()

    fun nextSendBatch(): List<EmailQueueEntry> =
        select { tries eq 0 }
            .orderBy(createdAt)
            .limit(100)
            .map { row ->
                EmailQueueEntry(
                    email = row[email].asCommon(),
                    lastTry = row[lastTry],
                    tries = row[tries],
                    lastFailMessage = row[lastFailMessage]
                )
            }


    fun nextRetryBatch(lastTryBefore : Instant): List<EmailQueueEntry> =
        select { (tries neq 0) and (lastTry less lastTryBefore)}
            .orderBy(lastTry)
            .limit(100)
            .map { row ->
                EmailQueueEntry(
                    email = row[email].asCommon(),
                    lastTry = row[lastTry],
                    tries = row[tries],
                    lastFailMessage = row[lastFailMessage]
                )
            }

}