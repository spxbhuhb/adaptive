package `fun`.adaptive.grove.doc.lib.compiler

import `fun`.adaptive.grove.doc.model.GroveDocExample
import `fun`.adaptive.log.LogLevel
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.persistence.*
import `fun`.adaptive.value.AvValueWorker
import kotlinx.io.files.Path

class GroveDocCompilation(
    val inPath: Path,
    val outPath: Path,
    val values : AvValueWorker,
    val baseUrl: String = "https://github.com/spxbhuhb/adaptive/tree/main"
) {

    val logger = getLogger("GroveDocCompiler")

    val inPathAbsolute: String = inPath.absolute().toString().replace('\\', '/')

    // Target output directories
    val outPathJunieLocal: Path = outPath.resolve("junie-local").also { it.ensure() }
    val outPathAIConsumer: Path = outPath.resolve("ai-consumer").also { it.ensure() }
    val outPathSite: Path = outPath.resolve("site").also { it.ensure() }

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

    fun outputDoc(target: DocTarget, group: String, path: Path, content: String) {
        val dir = when (target) {
            DocTarget.JunieLocal -> outPathJunieLocal
            DocTarget.AIConsumer -> outPathAIConsumer
            DocTarget.Site -> outPathSite // Site stores content in value store; we still ensure dir exists but do not write files here.
        }
        if (target == DocTarget.Site) return
        val subdir = when (group) {
            "definition" -> "definitions"
            "guide" -> "guides"
            "internals" -> "internals"
            else -> group
        }
        dir.resolve(subdir).ensure()
        dir.resolve(subdir).resolve(path.name).write(content, overwrite = true)
    }
}