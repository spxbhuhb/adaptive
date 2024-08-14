package `fun`.adaptive.email.service

import `fun`.adaptive.email.api.EmailApi
import `fun`.adaptive.email.model.Email
import `fun`.adaptive.email.model.EmailQueueEntry
import `fun`.adaptive.email.store.EmailQueue
import `fun`.adaptive.email.store.EmailTable
import `fun`.adaptive.email.worker.EmailWorker
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.backend.builtin.store
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.utility.UUID
import org.jetbrains.exposed.sql.transactions.transaction

class EmailService : EmailApi, ServiceImpl<EmailService> {

    // FIXME too expensive server fragment lookup in service implementation
    private val emailTable by store<EmailTable>()
    private val emailQueue by store<EmailQueue>()
    private val emailWorker by worker<EmailWorker>()

    override suspend fun send(recipients: String, subject: String, contentText: String, contentType: String) {
        transaction {
            val email = Email(UUID(), recipients, subject, contentText, contentType = contentType)

            emailTable += email

            val entry = EmailQueueEntry(email.id)

            emailQueue += entry
            emailWorker.normalQueue.trySend(entry)
        }
    }

}