package `fun`.adaptive.process

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit

actual fun runCommand(
    command: String,
    arguments: List<String>
): String {
    val processBuilder = ProcessBuilder(listOf(command) + arguments)
    processBuilder.redirectErrorStream(true)
    
    val process = processBuilder.start()
    val reader = BufferedReader(InputStreamReader(process.inputStream))
    
    val output = StringBuilder()
    var line: String?
    
    while (reader.readLine().also { line = it } != null) {
        output.append(line).append("\n")
    }
    
    // Wait for the process to complete
    if (!process.waitFor(60, TimeUnit.SECONDS)) {
        process.destroy()
        throw RuntimeException("Command execution timed out: $command")
    }
    
    // Check exit value
    val exitValue = process.exitValue()
    if (exitValue != 0) {
        throw RuntimeException("Command execution failed with exit code $exitValue: $command")
    }
    
    return output.toString().trim()
}