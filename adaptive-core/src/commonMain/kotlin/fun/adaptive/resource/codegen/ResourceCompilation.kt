package `fun`.adaptive.resource.codegen

import `fun`.adaptive.log.LogLevel
import `fun`.adaptive.resource.ResourceFile
import `fun`.adaptive.resource.ResourceFileSet
import `fun`.adaptive.resource.ResourceTypeQualifier
import `fun`.adaptive.utility.DangerousApi
import kotlinx.io.files.Path

class ResourceCompilation(
    val originalResourcesPath: Path,
    val packageName: String,
    val kmpSourceSet: String,
    val generatedCodePath: Path,
    val preparedResourcesPath: Path,
    val withFileQualifiers : Boolean = true,
    val withFileDefault : Boolean = true
) {
    class ResourceValue(
        val name : String,
        val value : ByteArray
    )

    class FileAndValues(
        val file: ResourceFile,
        val values: MutableMap<String, ResourceValue>
    )

    class CompilationReport(
        val severity: LogLevel,
        val message: String,
        val exception: Throwable? = null
    ) {
        override fun toString(): String =
            "$severity: $message\n    ${exception?.let { "\n    " + it.stackTraceToString() } ?: ""}"
    }

    val resourceSetsByType = ResourceTypeQualifier.entries
        .map { it to mutableMapOf<String, ResourceFileSet<*>>() }
        .associate { it }

    val reports = mutableListOf<CompilationReport>()

    // TODO the description is not correct, what do we delete exactly?
    @DangerousApi("deletes everything in target path recursively if originalResourcesPath does not exist")
    fun compile() {

        copyFilesAndCollectResourceSets()

        if (reports.isNotEmpty()) {
            throw IllegalStateException(
                "Errors occurred during resource processing:\n" +
                    reports.joinToString("\n")
            )
        }

        ResourceTypeQualifier.entries.filter { it.isUnstructured }.forEach { type ->
            generateUnstructuredAccessors(type)
        }

        processStringResources()
    }

    fun compilationError(message: () -> String) {
        reports += CompilationReport(LogLevel.Error, message())
    }

    fun compilationError(ex : Throwable, message: () -> String) {
        reports += CompilationReport(LogLevel.Error, message(), ex)
    }

}