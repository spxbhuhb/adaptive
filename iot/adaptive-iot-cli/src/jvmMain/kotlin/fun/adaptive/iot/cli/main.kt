package `fun`.adaptive.iot.cli

import `fun`.adaptive.iot.point.AioPointApi
import `fun`.adaptive.ktor.api.webSocketTransport
import `fun`.adaptive.lib.util.bytearray.ListenerByteArrayQueue
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.service.factory.BasicServiceImplFactory
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.clearedTestPath
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.builtin.AvBoolean
import `fun`.adaptive.value.builtin.AvDouble
import `fun`.adaptive.value.item.AvStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant

fun main() {
    val listener = ListenerByteArrayQueue(12675, clearedTestPath(), 1024L * 1024L, byteArrayOf(0x0A, 0x0A, 0x0A, 0x0A))
    listener.start()

    runBlocking {

        val pointService = getService<AioPointApi>(webSocketTransport("http://localhost:8080").start(BasicServiceImplFactory()))

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

                val curVal: AvValue?

                when (signature) {
                    "I" -> curVal = AvDouble(UUID.nil(), timestamp, AvStatus(flags), point, sValue.toDouble())
                    "Z" -> curVal = AvBoolean(UUID.nil(), timestamp, AvStatus(flags), point, sValue.toBoolean())
                    else -> {
                        println("unknown signature in message (dropped): $message")
                        curVal = null
                    }
                }

                if (curVal != null) {
                    pointService.setCurVal(curVal)
                }

                println("SUCCESS: $message")

                listener.queue.dequeue()

            } catch (e: Exception) {
                println("error while processing message : $message")
                e.printStackTrace()
                delay(30000)
            }
        }
    }
}

