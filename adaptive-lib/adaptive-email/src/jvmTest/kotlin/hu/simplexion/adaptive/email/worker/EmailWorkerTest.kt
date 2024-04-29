package hu.simplexion.adaptive.email.worker

class EmailWorkerTest {

//    companion object {
//
//        val wiser = Wiser()
//
//        @BeforeClass
//        @JvmStatic
//        fun setup() {
//            wiser.setPort(2500) // Default is 25
//            wiser.start()
//        }
//
//        @AfterClass
//        @JvmStatic
//        fun teardown() {
//            wiser.stop()
//        }
//
//    }
//
//    @Test
//    fun basic() {
//        wiser.messages.clear()
//
//        integratedWithSo(debugExposed = true, withTransaction = false) { _, so ->
//            prepare(so)
//
//            transaction {
//                runBlocking {
//                    emailImpl(so).send("noreply@simplexion.hu", "Hello", contentText = "World")
//                }
//            }
//
//            // withTimeout(1000) {
//            while (wiser.messages.size == 0) {
//                delay(30)
//            }
//            //  }
//
//            assertEquals(1, wiser.messages.size)
//
//            val message = wiser.messages.first()
//            val mimeMessage = message.mimeMessage
//            val content = mimeMessage.content as MimeMultipart
//
//            assertEquals("Hello", mimeMessage.subject)
//            assertEquals(1, content.count)
//
//            val part = content.getBodyPart(0)
//
//            assertEquals("text/plain; charset=UTF-8", part.contentType)
//            assertEquals("World", part.content)
//        }
//    }
//
//    suspend fun prepare(so: ServiceContext) {
//        transaction {
//            runBlocking {
//                val workerUuid = workerImpl(so).add(WorkerRegistration().also {
//                    it.provider = EmailWorkerProvider.PROVIDER_UUID
//                    it.name = "Test Email Worker"
//                    it.enabled = false
//                })
//
//                putSystemSettings(workerUuid, EmailSettings().also {
//                    it.host = "localhost"
//                    it.port = 2500
//                    it.username = "noreply@simplexion.hu"
//                    it.password = "helloworld"
//                    it.protocol = "smtp"
//                    it.tls = false
//                    it.auth = true
//                })
//
//                workerImpl(so).enable(workerUuid)
//            }
//        }
//    }

}