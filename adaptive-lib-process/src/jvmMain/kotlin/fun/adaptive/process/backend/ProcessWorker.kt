package `fun`.adaptive.process.backend

import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.service.transport.StreamServiceCallTransport
import `fun`.adaptive.wireformat.api.Json
import `fun`.adaptive.wireformat.api.Proto
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.isActive
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.coroutines.cancellation.CancellationException

class ProcessWorker(
    val command: Array<out String>,
    val environment: Map<String, String> = emptyMap(),
    val workingDirectory: String? = null,
    val wireFormat: String,
) : WorkerImpl<ProcessWorker> {

    override suspend fun run() {

        val builder = ProcessBuilder(*command)
            .directory(workingDirectory?.let { File(it) }) // null means current process's working directory
            .redirectInput(ProcessBuilder.Redirect.PIPE) // DriverCommand from driver jar
            .redirectOutput(ProcessBuilder.Redirect.PIPE) // DriverCommand to driver jar
            .redirectError(ProcessBuilder.Redirect.INHERIT) // driver error channel

        builder.environment().also { env ->
            for ((key, value) in environment) {
                env[key] = value
            }
        }

        val wireFormatProvider =
            when (wireFormat.lowercase()) {
                "proto" -> Proto
                "json" -> Json
                else -> throw IllegalArgumentException("invalid wire format: $wireFormat, expected proto or json")
            }

        val process = builder.start()

        logger.info("${command.contentToString()} started, PID: ${process.pid()}")

        val safeAdapter = adapter !!

        val transport = StreamServiceCallTransport(
            scope,
            process.inputStream,
            process.outputStream,
            safeAdapter,
            wireFormatProvider
        )

        try {

            transport.incoming()

        } catch (_: CancellationException) {
            // shutdown, no error to be logged there
            currentCoroutineContext().ensureActive()
        } catch (ex: Exception) {
            transport.transportLog.error(ex)
        }

        while (scope.isActive && process.isAlive) {
            try {
                process.waitFor(1000, TimeUnit.MILLISECONDS)
            } catch (_: InterruptedException) {
                return
            }
        }

        process.destroy()
        process.waitFor(5000, TimeUnit.MILLISECONDS)
        process.destroyForcibly()

        logger.info("${command.contentToString()} stopped, PID: ${process.pid()}, exit code: ${process.exitValue()}")
    }

}