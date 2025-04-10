package `fun`.adaptive.email.api

import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface EmailApi {

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