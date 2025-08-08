package `fun`.adaptive.grove.doc.lib.compiler

import `fun`.adaptive.grove.doc.model.GroveDocExample
import `fun`.adaptive.log.LogLevel
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.persistence.*
import `fun`.adaptive.value.AvValueWorker
import kotlinx.io.files.Path

class GroveDocCompilation(
    val inPath: Path,
    val mdOutPath: Path,
    val values : AvValueWorker,
    val baseUrl: String = "https://github.com/spxbhuhb/adaptive/tree/main",
    // Output toggles (defaults based on recommendation: Plain + Separated ON; Human-readable + Merged OFF)
    val producePlain: Boolean = true,
    val produceTrainingSeparated: Boolean = true,
    val produceHumanReadable: Boolean = false,
    val produceTrainingMerged: Boolean = false
) {

    val logger = getLogger("GroveDocCompiler")

    val inPathAbsolute: String = inPath.absolute().toString().replace('\\', '/')

    val outPathHumanReadable: Path = mdOutPath.resolve("human-readable").also { if (produceHumanReadable) it.ensure() }

    val outPathTrainingSeparated: Path = mdOutPath.resolve("separated").also { if (produceTrainingSeparated) it.ensure() }
    val outPathTrainingMerged: Path = mdOutPath.resolve("merged").also { if (produceTrainingMerged) it.ensure() }

    // Plain Markdown (GitHub-friendly) output directory
    val outPathPlain: Path = mdOutPath.resolve("plain").also { if (producePlain) it.ensure() }

    var notifications = mutableListOf<GroveDocNotification>()

    var reportedLinks = mutableSetOf<Link>()

    internal val fileCollector = FileCollector(this)

    val exampleGroups = mutableMapOf<String, List<GroveDocExample>>()

    fun warn(message: String, paths: List<Path> = emptyList()) {
        notifications.add(GroveDocNotification(LogLevel.Warning, message, paths.map { it.toString() }))
        logger.warning((listOf(message) + paths.map { it.toString() }).joinToString("\n"))
    }

    fun normalizedName(name: String) =
        name
            .substringBeforeLast('.')
            .trim()
            .trimEnd('?')
            .lowercase()

    fun outputHumanReadable(type: String, path: Path, content: String) {
        if (!produceHumanReadable) return
        outPathHumanReadable.resolve(type + "-" + path.name).write(content, overwrite = true)
    }

    fun outputPlain(type: String, path: Path, content: String) {
        if (!producePlain) return
        outPathPlain.resolve(type + "-" + path.name).write(content, overwrite = true)
    }

    /**
     * Outputs training content in two modes:
     *  - separated: one file per source with XML comments
     *  - merged: one file per subproject that combines definitions and guides without XML comments
     */
    fun outputTraining(subprojects : List<Subproject>, type: String, path: Path, content: String) {

        // ---- separated output ----
        if (produceTrainingSeparated) {
            val contentWithHeader = "<!-- name: ${path.name} -->\n<!-- type: $type -->\n\n$content\n\n".encodeToByteArray()
            outPathTrainingSeparated.resolve(type + "-" + path.name).write(contentWithHeader, overwrite = true)
        }

        // ---- merged output ----
        if (produceTrainingMerged) {
            val abs = path.absolute().toString().replace('\\', '/')
            val subproject = subprojects.firstOrNull { abs.startsWith(it.absolutePath) } ?: return

            val out = outPathTrainingMerged.resolve("${subproject.name}.md")

            out.append((content + "\n\n").encodeToByteArray())
        }
    }
}