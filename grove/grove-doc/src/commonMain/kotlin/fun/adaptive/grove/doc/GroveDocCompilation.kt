package `fun`.adaptive.grove.doc

import `fun`.adaptive.log.LogLevel
import `fun`.adaptive.utility.append
import `fun`.adaptive.utility.resolve
import `fun`.adaptive.utility.write
import kotlinx.io.files.Path

class GroveDocCompilation {

    var baseUrl: String = "https://github.com/spxbhuhb/adaptive/tree/main"

    lateinit var inPath : Path
    lateinit var inPathAbsolute: String
    lateinit var outPathAITraining: Path
    lateinit var outPathHumanReadable: Path
    lateinit var outPathAIMerged: Path

    var notifications = mutableListOf<GroveDocNotification>()

    var reportedLinks = mutableSetOf<Link>()

    internal val fileCollector = FileCollector(this)

    fun warn(message: String, paths: List<Path> = emptyList()) {
        notifications.add(GroveDocNotification(LogLevel.Warning, message, paths.map { it.toString() }))
    }

    fun normalizedName(name: String) =
        name
            .substringBeforeLast('.')
            .trim()
            .trimEnd('?')
            .lowercase()

    fun outputHumanReadable(type: String, path: Path, content: String) {
        outPathHumanReadable.resolve(path.name).write(content)
    }

    fun outputTraining(type: String, path: Path, content: String) {
        val contentWithHeader = "<!-- name: ${path.name} -->\n<!-- type: $type -->\n\n$content\n\n".encodeToByteArray()
        outPathAITraining.resolve(path.name).write(contentWithHeader)
        outPathAIMerged.append(contentWithHeader)
    }

}