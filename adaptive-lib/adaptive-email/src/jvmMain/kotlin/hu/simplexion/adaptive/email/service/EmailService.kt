package hu.simplexion.adaptive.email.service

import hu.simplexion.adaptive.email.api.EmailApi
import hu.simplexion.adaptive.email.model.Email
import hu.simplexion.adaptive.email.model.EmailQueueEntry
import hu.simplexion.adaptive.email.store.EmailQueue
import hu.simplexion.adaptive.email.store.EmailTable
import hu.simplexion.adaptive.email.worker.EmailWorker
import hu.simplexion.adaptive.server.component.store
import hu.simplexion.adaptive.server.component.worker
import hu.simplexion.adaptive.service.ServiceContext
import hu.simplexion.adaptive.service.ServiceImpl
import org.jetbrains.exposed.sql.transactions.transaction

class EmailService : EmailApi, ServiceImpl<EmailService> {

    // FIXME too expensive server fragment lookup in service implementation
    private val emailTable by store<EmailTable>()
    private val emailQueue by store<EmailQueue>()
    private val emailWorker by worker<EmailWorker>()

    override fun newInstance(serviceContext: ServiceContext): EmailService {
        return EmailService().also {
            it.serverAdapter = this.serverAdapter
        }
    }

    override suspend fun send(recipients: String, subject: String, contentText: String, contentType: String) {
        transaction {
            val email = Email(recipients, subject, contentText, contentType = contentType)

            emailTable.insert(email)

            val entry = EmailQueueEntry(email.uuid)

            emailQueue.insert(entry)
            emailWorker.normalQueue.trySend(entry)
        }
    }

}