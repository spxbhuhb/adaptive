package hu.simplexion.adaptive.email.worker

import hu.simplexion.adaptive.base.adaptive
import hu.simplexion.adaptive.email.api.EmailApi
import hu.simplexion.adaptive.email.service.EmailService
import hu.simplexion.adaptive.email.store.EmailQueue
import hu.simplexion.adaptive.email.store.EmailTable
import hu.simplexion.adaptive.exposed.InMemoryDatabase
import hu.simplexion.adaptive.server.AdaptiveServerAdapter
import hu.simplexion.adaptive.server.builtin.service
import hu.simplexion.adaptive.server.builtin.store
import hu.simplexion.adaptive.server.builtin.worker
import hu.simplexion.adaptive.server.server
import hu.simplexion.adaptive.service.getService
import hu.simplexion.adaptive.server.setting.dsl.inline
import hu.simplexion.adaptive.server.setting.dsl.settings
import hu.simplexion.adaptive.service.defaultServiceCallTransport
import hu.simplexion.adaptive.service.defaultServiceImplFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.AfterClass
import org.junit.BeforeClass
import org.subethamail.wiser.Wiser
import javax.mail.internet.MimeMultipart
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class EmailWorkerTest {

    companion object {

        val wiser = Wiser()

        @BeforeClass
        @JvmStatic
        fun setup() {
            wiser.setPort(2500) // Default is 25
            wiser.start()
        }

        @AfterClass
        @JvmStatic
        fun teardown() {
            wiser.stop()
        }

    }

    @Test
    fun basic() {
        wiser.messages.clear()

        val adapter = server {

            settings {
                inline(
                    "EMAIL_HOST" to "localhost",
                    "EMAIL_PORT" to 2500,
                    "EMAIL_USERNAME" to "noreply@simplexion.hu",
                    "EMAIL_PASSWORD" to "helloworld",
                    "EMAIL_PROTOCOL" to "smtp",
                    "EMAIL_TLS" to false,
                    "EMAIL_AUTH" to true,
                    "EMAIL_RETRY_INTERVAL" to 5,
                    "EMAIL_RETRY_CHECK_INTERVAL" to 5000,
                    "EMAIL_LIVE" to false,
                    "EMAIL_DEBUG" to false,
                    "TEST_EMAIL" to "noreply@simplexion.hu"
                )
            }

            worker { InMemoryDatabase() }

            store { EmailTable() }
            store { EmailQueue() }
            worker { EmailWorker() }
            service { EmailService() }

        }

        val expectRecipient = "noreply@simplexion.hu"
        val expectSubject = "Hello"
        val expectContent = "World"

        runBlocking {

            defaultServiceImplFactory += adapter.single<EmailService>()

            getService<EmailApi>().send(expectRecipient, expectSubject, expectContent)

            withTimeout(5.seconds) {
                while (wiser.messages.size == 0) {
                    delay(30.milliseconds)
                }
            }
        }

        assertEquals(1, wiser.messages.size)

        val message = wiser.messages.first()
        val mimeMessage = message.mimeMessage
        val content = mimeMessage.content as MimeMultipart
        val part = content.getBodyPart(0)

        assertEquals(expectRecipient, message.envelopeReceiver)
        assertEquals(expectSubject, mimeMessage.subject)
        assertEquals(1, content.count)
        assertEquals("text/plain; charset=UTF-8", part.contentType)
        assertEquals(expectContent, part.content)

        transaction {
            val emailTable = adapter.single<EmailTable>()

            val emails = emailTable.all()
            assertEquals(1, emails.size)

            val email = emails[0]
            assertEquals(expectRecipient, email.recipients)
            assertEquals(expectSubject, email.subject)
            assertEquals(expectContent, email.content)

            val emailQueue = adapter.single<EmailQueue>()
            assertEquals(0, emailQueue.count())
        }
    }

}