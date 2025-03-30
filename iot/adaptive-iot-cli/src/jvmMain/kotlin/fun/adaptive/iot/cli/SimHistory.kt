package `fun`.adaptive.iot.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.parameters.arguments.argument
import `fun`.adaptive.lib.util.bytearray.SenderByteArrayQueue
import `fun`.adaptive.utility.UUID
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.io.files.Path
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

fun main() {
    SimHistory().main(arrayOf("./iot/adaptive-iot-cli/var/sim-history", "0195e531-f30e-769f-8000-00007581b29e"))
}

class SimHistory : CliktCommand(name = "sim-history") {

    private val queuePath by argument("path", help = "Path to the send queue")
    private val historyId by argument("history-id", help = "UUID of the history")

    lateinit var curValQueue: SenderByteArrayQueue

    override fun run() {

        UUID<Any>(historyId) // to check that the id is indeed an uuid

        curValQueue = SenderByteArrayQueue(
            "127.0.0.1",
            12675,
            Path(queuePath),
            1024L * 1024L,
            byteArrayOf(0x0A, 0x0A, 0x0A, 0x0A)
        )

        curValQueue.start()

        val end = Clock.System.now()
        val start = end.minus(30.days)

        generateTemperatureHistory(start, end)

        while (!curValQueue.isEmpty) {
            Thread.sleep(1000)
        }

        curValQueue.stop()
    }

    fun append(timestamp: Instant, value: Double, flags: Int = 0) {
        curValQueue.enqueue("$historyId D $timestamp $flags $value".toByteArray())
    }

    fun generateTemperatureHistory(
        start: Instant,
        end: Instant,
        averageInterval: Duration = 5.minutes,
        minTemp: Double = 15.0,
        maxTemp: Double = 25.0,
        noiseFactor: Double = 0.5
    ) {
        val random = Random.Default
        var current = start
        var currentTemp = (minTemp + maxTemp) / 2.0

        while (current < end) {
            // Simulate daily variation (sine wave approximation)
            val hour = current.toLocalDateTime(TimeZone.UTC).hour
            val dailyOffset = kotlin.math.sin((hour / 24.0) * 2 * Math.PI)
            val baseTemp = minTemp + (maxTemp - minTemp) * (0.5 + 0.5 * dailyOffset)

            // Add small random noise for realism
            currentTemp = baseTemp + random.nextDouble(- noiseFactor, noiseFactor)

            append(current, currentTemp)

            // Advance by slightly randomized interval (±20% of average interval)
            val jitter = averageInterval.inWholeMilliseconds * 0.2
            val nextIntervalMs = averageInterval.inWholeMilliseconds +
                random.nextLong(- jitter.toLong(), jitter.toLong())
            current = current.plus(nextIntervalMs.milliseconds)
        }
    }

}