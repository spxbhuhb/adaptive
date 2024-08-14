package `fun`.adaptive.email.worker

import `fun`.adaptive.email.api.EmailApi
import `fun`.adaptive.email.service.EmailService
import `fun`.adaptive.email.store.EmailQueue
import `fun`.adaptive.email.store.EmailTable
import `fun`.adaptive.exposed.inMemoryH2
import `fun`.adaptive.foundation.query.singleImpl
import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.backend.builtin.store
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.setting.dsl.inline
import `fun`.adaptive.backend.setting.dsl.settings
import `fun`.adaptive.service.getService
import `fun`.adaptive.service.testing.TestServiceTransport
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

        val adapter = backend {

            inMemoryH2()

            store { EmailTable }
            store { EmailQueue }
            worker { EmailWorker() }
            service { EmailService() }

        }

        val expectRecipient = "noreply@simplexion.hu"
        val expectSubject = "Hello"
        val expectContent = "World"

        runBlocking {


            val transport = TestServiceTransport(serviceImplFactory = adapter)

            getService<EmailApi>(transport).send(expectRecipient, expectSubject, expectContent)

            withTimeout(15.seconds) { // might be slow during complete build
                while (wiser.messages.isEmpty()) {
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
            val emailTable = adapter.singleImpl<EmailTable>()

            val emails = emailTable.all()
            assertEquals(1, emails.size)

            val email = emails[0]
            assertEquals(expectRecipient, email.recipients)
            assertEquals(expectSubject, email.subject)
            assertEquals(expectContent, email.content)

            val emailQueue = adapter.singleImpl<EmailQueue>()
            assertEquals(0, emailQueue.count())
        }
    }

}