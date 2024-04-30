package hu.simplexion.adaptive.email.api

import hu.simplexion.adaptive.service.Service

interface EmailApi : Service {

    /**
     * Send an e-mail from the system. This API is not meant for
     * use from the clients directly. Instead, it is typically
     * called from other service implementations to send e-mails
     * when an event happens.
     *
     * Requires technical administrator role or internal call.
     */
    suspend fun send(
        recipients: String,
        subject: String,
        contentText: String,
        contentType: String = "text/plain"
    )

}