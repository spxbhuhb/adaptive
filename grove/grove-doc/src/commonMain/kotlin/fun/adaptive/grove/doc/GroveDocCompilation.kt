package `fun`.adaptive.grove.doc

import `fun`.adaptive.log.LogLevel
import `fun`.adaptive.utility.append
import `fun`.adaptive.utility.resolve
import `fun`.adaptive.utility.write
import kotlinx.io.files.Path

class GroveDocCompilation {

    lateinit var inPath : Path
    lateinit var outPathSeparated : Path
    lateinit var outPathMerged : Path

    var notifications = mutableListOf<GroveDocNotification>()

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

    fun output(type : String,  path : Path, content : String) {
        outPathSeparated.resolve(path.name).write(content)

        val contentWithHeader = "<!-- name: ${path.name} -->\n<!-- type: $type -->\n\n$content\n\n"

        outPathMerged.append(contentWithHeader.encodeToByteArray())
    }

}