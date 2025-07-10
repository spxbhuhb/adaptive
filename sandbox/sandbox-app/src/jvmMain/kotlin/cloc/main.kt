import java.io.File
import java.io.BufferedReader

/**
 * Data class to hold the summarized CLOC statistics for a given source set.
 * @property files The total number of files counted.
 * @property blank The total number of blank lines.
 * @property comment The total number of comment lines.
 * @property code The total number of code lines.
 */
data class ClocSummary(
    val files: Int,
    val blank: Int,
    val comment: Int,
    val code: Int
) {
    // Helper function to add two ClocSummary objects
    operator fun plus(other: ClocSummary): ClocSummary {
        return ClocSummary(
            files + other.files,
            blank + other.blank,
            comment + other.comment,
            code + other.code
        )
    }
}

/**
 * Main function to run the CLOC statistics collection.
 * It expects the project root path as an optional command-line argument.
 * If no argument is provided, it defaults to the current directory.
 */
fun main(args: Array<String>) {
    // Determine the project root path. Defaults to the current directory if no argument is provided.
    val projectRootPath = args.firstOrNull() ?: "."
    val projectRoot = File(projectRootPath)

    // Validate if the provided path is a directory.
    if (!projectRoot.isDirectory) {
        println("Error: '$projectRootPath' is not a valid directory.")
        return
    }

    println("Starting CLOC scan for Kotlin Multiplatform project at: ${projectRoot.canonicalPath}")
    println("Ensure 'cloc' is installed and available in your system's PATH.")

    // Define common Kotlin Multiplatform source set names.
    // This list can be extended if you have custom source sets.
    val sourceSets = listOf(
        "commonMain", "jsMain", "jvmMain", "androidMain", "iosMain",
        "commonTest", "jsTest", "jvmTest", "androidTest", "iosTest",
        "desktopMain", "wasmJsMain", "watchosMain", "tvosMain", "macosMain", "linuxMain", "mingwMain"
    )

    // Mutable map to store CLOC summary results for each identified source set.
    // Key: "moduleName/sourceSetName", Value: ClocSummary object (nullable in case of error).
    val results = mutableMapOf<String, ClocSummary?>()

    /**
     * Recursively processes directories to find Kotlin Multiplatform modules and their source sets.
     * @param directory The current directory to scan.
     */
    fun processDirectory(directory: File) {
        directory.listFiles()?.forEach { file ->
            if (file.isDirectory) {
                // Check if the current directory is a potential module root.
                // A module typically contains a 'build.gradle.kts' or 'settings.gradle.kts' file.
                val isModuleRoot = file.listFiles()?.any {
                    it.name == "build.gradle.kts" || it.name == "settings.gradle.kts" || it.name == "build.gradle"
                } == true

                // If it's a module root, look for its 'src' directory and then source sets.
                if (isModuleRoot) {
                    val srcDir = File(file, "src")
                    if (srcDir.isDirectory) {
                        println("\nFound module: ${file.name}")
                        sourceSets.forEach { sourceSet ->
                            val sourceSetDir = File(srcDir, sourceSet)
                            if (sourceSetDir.isDirectory) {
                                println("  Counting lines for: ${sourceSetDir.name} in module ${file.name}")
                                // Run cloc on the source set directory and store the summary.
                                val clocSummary = runCloc(sourceSetDir)
                                results["${file.name}/${sourceSet}"] = clocSummary
                            }
                        }
                    }
                }
                // Recursively call processDirectory to find modules in subdirectories,
                // regardless of whether the current 'file' is a module root itself.
                processDirectory(file)
            }
        }
    }

    // Start processing from the project root.
    processDirectory(projectRoot)

    // Print the collected CLOC statistics per module/source set.
    println("\n--- CLOC Statistics by Module and Source Set ---")
    if (results.isEmpty()) {
        println("No Kotlin Multiplatform source sets found or 'cloc' failed to run for any directory.")
    } else {
        results.forEach { (sourceSetPath, summary) ->
            // Extract module name and source set name from the path for display
            val parts = sourceSetPath.split('/')
            val moduleName = parts.firstOrNull() ?: "Unknown Module"
            val sourceSetName = parts.lastOrNull() ?: "Unknown Source Set"

            println("\n### Statistics for '$sourceSetName' (Module: '$moduleName') ###")
            if (summary != null) {
                println("  Files: ${summary.files}")
                println("  Blank: ${summary.blank}")
                println("  Comment: ${summary.comment}")
                println("  Code: ${summary.code}")
                println("  Total Lines (Blank + Comment + Code): ${summary.blank + summary.comment + summary.code}")
            } else {
                println("  Failed to retrieve CLOC summary for this source set.")
            }
        }

        // Calculate and print totals per source set type (e.g., all commonMain together)
        val sourceSetTotals = mutableMapOf<String, ClocSummary>()
        results.forEach { (sourceSetPath, summary) ->
            if (summary != null) {
                // Extract just the source set name (e.g., "commonMain" from "moduleName/commonMain")
                val sourceSetName = sourceSetPath.substringAfterLast('/')
                sourceSetTotals.merge(sourceSetName, summary) { old, new -> old + new }
            }
        }

        println("\n--- Aggregated CLOC Statistics by Source Set Type ---")
        if (sourceSetTotals.isEmpty()) {
            println("No aggregated source set statistics available.")
        } else {
            sourceSetTotals.forEach { (sourceSetName, summary) ->
                println("\n### Total Statistics for All '$sourceSetName' Source Sets ###")
                println("  Files: ${summary.files}")
                println("  Blank: ${summary.blank}")
                println("  Comment: ${summary.comment}")
                println("  Code: ${summary.code}")
                println("  Total Lines (Blank + Comment + Code): ${summary.blank + summary.comment + summary.code}")
            }
        }
    }
    println("\n--- End of CLOC Statistics ---")
}

/**
 * Executes the 'cloc' command on the specified directory and returns its parsed summary.
 * This version specifically extracts data from the "Kotlin" line.
 * @param directory The directory on which to run cloc.
 * @return A ClocSummary object if parsing is successful, null otherwise.
 */
fun runCloc(directory: File): ClocSummary? {
    // Construct the command to execute cloc.
    val command = listOf("cloc", directory.absolutePath)

    return try {
        val process = ProcessBuilder(command)
            .redirectErrorStream(true) // Merge stderr into stdout
            .start()

        val reader = BufferedReader(process.inputStream.reader())
        val output = reader.readText()
        val exitCode = process.waitFor()

        if (exitCode != 0) {
            System.err.println("Error running cloc for ${directory.absolutePath}. Exit code: $exitCode")
            System.err.println("CLOC output (error/info): \n$output")
            return null // Return null on cloc execution error
        }

        // Regex to find the "Kotlin" line and capture the numbers.
        // It looks for "Kotlin" followed by whitespace, then captures four groups of digits.
        val kotlinLineRegex = "Kotlin\\s*(\\d+)\\s*(\\d+)\\s*(\\d+)\\s*(\\d+)".toRegex()
        val matchResult = kotlinLineRegex.find(output)

        if (matchResult != null) {
            // Extract captured groups and convert them to Int.
            val (files, blank, comment, code) = matchResult.destructured
            ClocSummary(files.toInt(), blank.toInt(), comment.toInt(), code.toInt())
        } else {
            System.err.println("Could not find 'Kotlin' line in cloc output for ${directory.absolutePath}.")
            System.err.println("CLOC output: \n$output")
            null // Return null if Kotlin line is not found
        }
    } catch (e: Exception) {
        System.err.println("Failed to execute cloc command for ${directory.absolutePath}: ${e.message}")
        null // Return null on exception
    }
}
