package `fun`.adaptive.iot.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.parameters.arguments.argument
import `fun`.adaptive.auth.api.AuthSessionApi
import `fun`.adaptive.auth.backend.AuthSessionService
import `fun`.adaptive.iot.point.AioPointApi
import `fun`.adaptive.ktor.api.webSocketTransport
import `fun`.adaptive.lib.util.bytearray.ListenerByteArrayQueue
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.ensure
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.builtin.AvBoolean
import `fun`.adaptive.value.builtin.AvDouble
import `fun`.adaptive.value.builtin.AvString
import `fun`.adaptive.value.item.AvStatus
import `fun`.adaptive.wireformat.api.Proto
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import kotlinx.io.files.Path

fun main() {
    CurValUpload().main(arrayOf("./iot/adaptive-iot-cli/var/curval", "http://localhost:8080", "so", "so"))
}

class CurValUpload : CliktCommand(name = "curval-upload") {
    private val logger = getLogger("curval-upload")

    private val queuePathArg by argument("queue-path", help = "Path to the queue directory")
    private val urlArg by argument("url", help = "WebSocket URL for the point service")
    private val username by argument("username", help = "Username for the point service")
    private val password by argument("password", help = "Password for the point service")

    override fun run() {
        val queuePath = Path(queuePathArg).ensure()
        val url = urlArg

        val listener = ListenerByteArrayQueue(12675, queuePath, 1024L * 1024L, byteArrayOf(0x0A, 0x0A, 0x0A, 0x0A))
        listener.start()

        runBlocking {
            logger.info("CurVal upload to $url started")

            val transport = webSocketTransport(url, wireFormatProvider = Proto).start()

            val sessionService = getService<AuthSessionApi>(transport)
            sessionService.signIn(username, password)

            val pointService = getService<AioPointApi>(transport)

            while (true) {
                var message = ""
                try {
                    message = listener.queue.peek().decodeToString()

                    val fields = message.trim().split(" ")

                    val point = UUID<AvValue>(fields[0])
                    val signature = fields[1]
                    val timestamp = Instant.parse(fields[2])
                    val flags = fields[3].toInt()
                    val sValue = fields[4]

                    val curVal: AvValue? = when (signature) {
                        "D"-> AvDouble(UUID.nil(), timestamp, AvStatus(flags), point, sValue.toDouble())
                        "I" -> AvDouble(UUID.nil(), timestamp, AvStatus(flags), point, sValue.toDouble())
                        "Z" -> AvBoolean(UUID.nil(), timestamp, AvStatus(flags), point, sValue.toBoolean())
                        "T" -> AvString(UUID.nil(), timestamp, AvStatus(flags), point, sValue)
                        else -> {
                            logger.warning("Unknown signature in message (dropped): $message")
                            null
                        }
                    }

                    if (curVal != null) {
                        pointService.setCurVal(curVal)
                    }

                    logger.info("UPLOADED: $message")
                    listener.queue.dequeue()

                } catch (e: Exception) {
                    logger.error("Error while processing message: $message", e)
                    delay(30000)
                }
            }
        }
    }
}