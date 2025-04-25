package `fun`.adaptive.iot.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.parameters.arguments.argument
import `fun`.adaptive.lib.util.bytearray.SenderByteArrayQueue
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.list
import `fun`.adaptive.utility.readString
import kotlinx.datetime.Instant
import kotlinx.io.files.Path

fun main() {
    // 0195e531-f30e-769f-8000-00007581b29e  -- hom
    // 0195e531-f375-70b4-bfff-ffffcde14582  -- para
    UploadTextHistory().main(
        arrayOf(
            "/Users/tiz/src/adaptive-iot/adaptive-iot-zigbee/var/history",
            "./iot/adaptive-iot-cli/var/upload-text-history"
        )
    )
}

class Record(
    val timestamp: Instant,
    val value: ByteArray
) : Comparable<Record> {
    override fun compareTo(other: Record): Int {
        return timestamp.compareTo(other.timestamp)
    }
}

class UploadTextHistory : CliktCommand(name = "upload-text-history") {

    private val historyPath by argument("history-path", help = "Path to the textual history")
    private val queuePath by argument("queue-path", help = "Path to the send queue")

    lateinit var curValQueue: SenderByteArrayQueue

    val records = mutableListOf<Record>()

    override fun run() {

        curValQueue = SenderByteArrayQueue(
            "127.0.0.1",
            12675,
            Path(queuePath),
            1024L * 1024L,
            byteArrayOf(0x0A, 0x0A, 0x0A, 0x0A)
        )

        curValQueue.start()

        processHistoryDir(Path(historyPath))

        records.sort()

        records.forEach {
            curValQueue.enqueue(it.value)
        }

        while (! curValQueue.isEmpty) {
            Thread.sleep(1000)
        }

        curValQueue.stop()
    }

    fun processHistoryDir(path: Path) {
        path.list().forEach {
            if (it.name.startsWith("device")) {
                processDevice(it)
            }
        }
    }

    fun processDevice(path: Path) {
        path.list().forEach {
            if (it.name.startsWith("point")) {
                processPoint(it)
            }
        }
    }

    fun processPoint(path: Path) {
        path.list().sortedBy { it.name }.forEach {
            if (it.name.endsWith(".txt")) {
                try {
                    uploadFile(it)
                } catch (e: Exception) {
                    println("error processing $it")
                    e.printStackTrace()
                    return@forEach
                }
            }
        }
    }

    fun uploadFile(path: Path) {
        val historyId = UUID<Any>(path.parent !!.name.substringAfter('.'))

        path.readString().lines().forEach {

            if (it.trim().isEmpty()) return@forEach

            val timestamp = Instant.parse(it.substringBefore(" "))
            val valueText = it.substringAfter(' ')

            when {
                valueText == "false" || valueText == "true" -> {
                    records += Record(timestamp, "$historyId Z $timestamp 0 $valueText".toByteArray())
                }

                valueText.toDoubleOrNull() != null -> {
                    records += Record(timestamp, "$historyId D $timestamp 0 $valueText".toByteArray())
                }

                else -> {
                    records += Record(timestamp, "$historyId T $timestamp 0 $valueText".toByteArray())
                }
            }
        }
    }

}