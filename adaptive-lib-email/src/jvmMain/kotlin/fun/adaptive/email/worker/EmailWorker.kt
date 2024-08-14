package `fun`.adaptive.email.worker

import com.sun.mail.util.MailConnectException
import `fun`.adaptive.email.model.EmailQueueEntry
import `fun`.adaptive.email.model.EmailStatus
import `fun`.adaptive.email.store.EmailQueue
import `fun`.adaptive.email.store.EmailTable
import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.backend.builtin.launch
import `fun`.adaptive.backend.builtin.store
import `fun`.adaptive.backend.setting.dsl.setting
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.Clock.System.now
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import javax.mail.*
import javax.mail.internet.*
import kotlin.time.Duration.Companion.minutes

class EmailWorker : WorkerImpl<EmailWorker> {

    val host by setting<String> { "EMAIL_HOST" }
    val port by setting<Int> { "EMAIL_PORT" }
    val username by setting<String> { "EMAIL_USERNAME" }
    val password by setting<String> { "EMAIL_PASSWORD" } // TODO secret
    val protocol by setting<String> { "EMAIL_PROTOCOL" }
    val auth by setting<Boolean> { "EMAIL_AUTH" }
    val tls by setting<Boolean> { "EMAIL_TLS" }
    val debug by setting<Boolean> { "EMAIL_DEBUG" }
    val retryInterval by setting<Long> { "EMAIL_RETRY_INTERVAL" }
    val retryLimit by setting<Int> { "EMAIL_RETRY_LIMIT" }
    val retryCheckInterval by setting<Long> { "EMAIL_RETRY_CHECK_INTERVAL" }
    val live by setting<Boolean> { "EMAIL_LIVE" }

    val emailStore by store<EmailTable>()
    val emailQueue by store<EmailQueue>()

    val normalQueue = Channel<EmailQueueEntry>(Channel.UNLIMITED)

    override suspend fun run() {

        launch { retry() }

        sendBatch { emailQueue.nextSendBatch() }

        for (entry in normalQueue) {
            if (! isActive) break
            sendBatch { emailQueue.nextSendBatch() }
        }

    }

    suspend fun retry() {
        while (isActive) {

            val lastTryBefore = Clock.System.now().minus(retryInterval.minutes)

            sendBatch { emailQueue.nextRetryBatch(lastTryBefore) }

            delay(retryCheckInterval)

        }
    }

    fun sendBatch(readBatch: () -> List<EmailQueueEntry>) {
        while (isActive) {
            val batch = transaction { readBatch() }

            if (batch.isEmpty()) return

            for (item in batch) {
                if (! isActive) break
                send(item)
            }
        }
    }

    private fun send(queueEntry: EmailQueueEntry, failMessage: String? = null) {
        val email = transaction { emailStore[queueEntry.email] }

        if (email.status.isFinal) {
            transaction {
                emailQueue.remove(email.id)
            }
            return
        }

        fun update(status: EmailStatus) {
            transaction {
                emailStore.setStatus(queueEntry.email, status)
                if (status in listOf(EmailStatus.Sent, EmailStatus.Failed)) {
                    emailQueue.remove(queueEntry.email)
                    // FIXME store the fail message
                } else {
                    emailQueue.update(
                        EmailQueueEntry(
                            queueEntry.email,
                            now(),
                            queueEntry.tries + 1,
                            null,
                            failMessage
                        )
                    )
                }
            }
        }

        try {
            val prop = Properties()

            prop["mail.smtp.auth"] = auth
            prop["mail.smtp.starttls.enable"] = if (tls) "true" else "false"
            prop["mail.smtp.host"] = host
            prop["mail.smtp.port"] = port
            prop["mail.smtp.protocol"] = protocol
            prop["mail.debug"] = debug

            val session = Session.getInstance(prop, object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(username, password)
                }
            })

            val message = MimeMessage(session)
            message.setFrom(InternetAddress(username))

            val address = if (live) {
                email.recipients
            } else {
                checkNotNull(setting<String> { "TEST_EMAIL" }.value) { "test mode without TEST_EMAIL setting" }
            }

            val recipient = try {
                InternetAddress.parse(address)
            } catch (ex: AddressException) {
                logger.error("failed to send email ${email.id}", ex)
                update(EmailStatus.Failed)
                return
            }

            message.setRecipients(Message.RecipientType.TO, recipient)
            message.setSubject(email.subject, "utf-8")

            val multipart = MimeMultipart()
            val bodyPart = MimeBodyPart()
            when (email.contentType) {
                "text/plain" -> bodyPart.setText(email.content, "UTF-8")
                "text/html" -> bodyPart.setContent(email.content, "text/html; charset=UTF-8")
                else -> throw IllegalStateException("body mime type (${email.contentType} must be 'text/plain' or 'text/html}")
            }
            bodyPart.disposition = Part.INLINE
            multipart.addBodyPart(bodyPart)

            message.setContent(multipart)

            try {

                Transport.send(message)

                update(EmailStatus.Sent)
                logger.info("email sent: ${email.id}")

            } catch (ex: AuthenticationFailedException) {

                // config error, keep the e-mail in the queue untouched
                logger.error("email server authentication fail", ex)

            } catch (ex: MailConnectException) {

                // config error or server is unavailable, keep the e-mail in the queue
                logger.error("email server authentication fail", ex)
                update(EmailStatus.RetryWait)

            } catch (ex: SendFailedException) {

                logger.error("error message from the mail server for email ${email.id}", ex)
                update(EmailStatus.Failed)

            } catch (ex: MessagingException) {

                // whatever error, keep the e-mail in the queue
                logger.error("failed to send email ${email.id}", ex)
                update(EmailStatus.RetryWait)

            }

        } catch (ex: Exception) {
            logger.error("failed to send email ${email.id}", ex)
            update(EmailStatus.Failed)
        }
    }

}