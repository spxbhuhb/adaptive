package hu.simplexion.adaptive.email.service

import hu.simplexion.adaptive.email.api.EmailApi
import hu.simplexion.adaptive.email.model.Email
import hu.simplexion.adaptive.email.model.EmailQueueEntry
import hu.simplexion.adaptive.email.store.EmailQueue
import hu.simplexion.adaptive.email.store.EmailTable
import hu.simplexion.adaptive.email.worker.EmailWorker
import hu.simplexion.adaptive.server.components.store
import hu.simplexion.adaptive.server.components.worker
import hu.simplexion.adaptive.service.ServiceImpl

class EmailService : EmailApi, ServiceImpl<EmailService> {

    // FIXME too expensive server fragment lookup in service implementation
    private val emailTable = store<EmailTable>()
    private val emailQueue = store<EmailQueue>()
    private val emailWorker = worker<EmailWorker>()

    override suspend fun send(recipients: String, subject: String, contentText: String, contentType: String) {
        val email = Email(recipients, subject, contentText, contentType = contentType)

        emailTable.insert(email)

        val entry = EmailQueueEntry(email.uuid)

        emailQueue.insert(entry)
        emailWorker.normalQueue.trySend(entry)
    }

}