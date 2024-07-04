package hu.simplexion.adaptive.email.store

import hu.simplexion.adaptive.email.model.Email
import hu.simplexion.adaptive.email.model.EmailQueueEntry
import hu.simplexion.adaptive.exposed.AdatTable
import hu.simplexion.adaptive.exposed.asCommon
import hu.simplexion.adaptive.exposed.uuidEq
import hu.simplexion.adaptive.utility.UUID
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object EmailQueue : AdatTable<EmailQueueEntry, EmailQueue>("email_queue") {

    val email = reference("email", EmailTable)
    val createdAt = timestamp("createdAt")
    val tries = integer("tries")
    val lastTry = timestamp("lastTry").nullable()
    val lastFailMessage = text("lastFailMessage").nullable()

    fun update(entry: EmailQueueEntry) {
        update({ email uuidEq entry.email }) {
            it[tries] = entry.tries
            it[lastTry] = entry.lastTry
            it[lastFailMessage] = entry.lastFailMessage
        }
    }

    fun remove(uuid: UUID<Email>) {
        deleteWhere { email uuidEq uuid }
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