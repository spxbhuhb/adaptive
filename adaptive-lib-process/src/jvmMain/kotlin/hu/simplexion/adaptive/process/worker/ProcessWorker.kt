package hu.simplexion.adaptive.process.worker

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

class ProcessWorker {

    var job: Job? = null

    fun onStart() {

        val java = System.getProperty("java.home") + "/bin/java"
        val jar = "a.jar"

        val builder = ProcessBuilder(java, "-jar", jar.toString())
            .directory(null) // null means current process's working directory
            .redirectInput(ProcessBuilder.Redirect.PIPE) // DriverCommand from driver jar
            .redirectOutput(ProcessBuilder.Redirect.PIPE) // DriverCommand to driver jar
            .redirectError(ProcessBuilder.Redirect.INHERIT) // driver error channel

        builder.environment().also { env ->
            env["NAME"] = ""
        }

        val process = builder.start()

        // logger.info("${jar.fileName} started, PID: ${process.pid()}")

        val writer = process.outputStream.bufferedWriter()
        val reader = process.inputStream.bufferedReader()

        job?.cancel()

        job = CoroutineScope(Executors.newSingleThreadExecutor().asCoroutineDispatcher()).launch {
            while (true) {
                toDriver.receive().also { line ->
                    val driverRootEntity = context[element.id].dict.asEntity()
                    driverRootEntity.logTrace100 { "toDriver: $line" }
                    writer.appendLine(line)
                    writer.flush()
                }
            }
        }

        CoroutineScope(Executors.newSingleThreadExecutor().asCoroutineDispatcher()).launch {

            while (process.isAlive) {

            }

            process.destroy()

            process.waitFor(5000, TimeUnit.MILLISECONDS)

            process.destroyForcibly()

            val exitValue = try {
                process.exitValue()
            } catch (ex: Exception) {
                "-"
            }

            // logger.info("${jar.fileName} finished, PID: ${process.pid()}, exitValue: $exitValue")

        }

    }

}