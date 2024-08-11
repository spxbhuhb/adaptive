package hu.simplexion.adaptive.email.service

import hu.simplexion.adaptive.email.api.EmailApi
import hu.simplexion.adaptive.email.model.Email
import hu.simplexion.adaptive.email.model.EmailQueueEntry
import hu.simplexion.adaptive.email.store.EmailQueue
import hu.simplexion.adaptive.email.store.EmailTable
import hu.simplexion.adaptive.email.worker.EmailWorker
import hu.simplexion.adaptive.server.builtin.ServiceImpl
import hu.simplexion.adaptive.server.builtin.store
import hu.simplexion.adaptive.server.builtin.worker
import hu.simplexion.adaptive.service.ServiceContext
import hu.simplexion.adaptive.utility.UUID
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