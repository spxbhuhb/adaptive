package hu.simplexion.adaptive.email.worker

import com.sun.mail.util.MailConnectException
import hu.simplexion.adaptive.email.model.EmailQueueEntry
import hu.simplexion.adaptive.email.model.EmailStatus
import hu.simplexion.adaptive.email.table.EmailQueue
import hu.simplexion.adaptive.email.table.EmailTable
import hu.simplexion.adaptive.server.components.WorkerImpl
import hu.simplexion.adaptive.server.components.store
import hu.simplexion.adaptive.settings.dsl.setting
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import java.util.*
import javax.mail.*
import javax.mail.internet.*
import kotlin.time.Duration.Companion.minutes

class EmailWorker : WorkerImpl<EmailWorker> {

    companion object {
        val logger = LoggerFactory.getLogger(EmailWorker::class.java) !!
    }

    val host by setting<String> { "EMAIL_HOST" }
    val port by setting<String> { "EMAIL_PORT" }
    val username by setting<String> { "EMAIL_USERNAME" }
    val password by setting<String> { "EMAIL_PASSWORD" } // TODO secret
    val protocol by setting<String> { "EMAIL_PROTOCOL" }
    val auth by setting<Boolean> { "EMAIL_AUTH" }
    val tls by setting<Boolean> { "EMAIL_TLS" }
    val debug by setting<Boolean> { "EMAIL_DEBUG" }
    val retryCheckInterval by setting<Long> { "EMAIL_RETRY_CHECK_INTERVAL" }
    val live by setting<Boolean> { "EMAIL_LIVE" }

    val emailStore = store<EmailTable>()
    val emailQueue = store<EmailQueue>()

    val normalQueue = Channel<EmailQueueEntry>(Channel.UNLIMITED)

    override suspend fun run(scope: CoroutineScope) {

        scope.launch { retry(scope) }

        loadNormalQueue()

        try {
            for (entry in normalQueue) {
                if (! scope.isActive) return
                if (sendPending() == 0) {
                    delay(500)
                    sendPending()
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace() // FIXME email processing exceptions
        }

    }

    fun loadNormalQueue() {
        transaction {
            emailQueue.all().filter { it.lastTry == null }.forEach { normalQueue.trySend(it) }
        }
    }

    fun sendPending(): Int {
        val pending = transaction {
            emailQueue.all().filter { it.lastTry == null }
        }

        pending.forEach {
            try {
                send(it)
            } catch (ex: CancellationException) {
                return 0
            }
        }

        return pending.size
    }

    suspend fun retry(scope: CoroutineScope) {
        while (scope.isActive) {

            val now = Clock.System.now().minus(30.minutes)

            val entries = transaction {
                emailQueue.all().filter { entry ->
                    entry.lastTry?.let { it < now } ?: false
                }
            }

            if (entries.isEmpty()) {
                delay(retryCheckInterval)
                continue
            }

            for (entry in entries) {
                if (! scope.isActive) return

                try {
                    send(entry)
                } catch (ex: CancellationException) {
                    return
                } catch (ex: Exception) {
                    scope.cancel()
                }
            }
        }
    }

    private fun send(queueEntry: EmailQueueEntry, failMessage: String? = null) {
        val email = transaction { emailStore[queueEntry.email] }

        if (email.status.isFinal) {
            transaction {
                emailQueue.remove(email.uuid)
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
                            queueEntry.tries + 1,
                            Clock.System.now(),
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
                logger.error("failed to send email ${email.uuid}", ex)
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
                logger.info("email sent: ${email.uuid}")

            } catch (ex: AuthenticationFailedException) {

                // config error, keep the e-mail in the queue
                logger.error("email server authentication fail", ex)
                update(EmailStatus.RetryWait)

            } catch (ex: MailConnectException) {

                // config error or server is unavailable, keep the e-mail in the queue
                logger.error("email server authentication fail", ex)
                update(EmailStatus.RetryWait)

            } catch (ex: SendFailedException) {

                logger.error("error message from the mail server for email ${email.uuid}", ex)
                update(EmailStatus.Failed)

            } catch (ex: MessagingException) {

                // whatever erroe, keep the e-mail in the queue
                logger.error("email send fail fail", ex)
                update(EmailStatus.RetryWait)

            }

        } catch (ex: Exception) {
            logger.error("failed to send email ${email.uuid}", ex)
            update(EmailStatus.Failed)
        }
    }

}