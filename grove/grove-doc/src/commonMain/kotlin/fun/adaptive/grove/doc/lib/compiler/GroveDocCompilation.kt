package `fun`.adaptive.grove.doc.lib.compiler

import `fun`.adaptive.log.LogLevel
import `fun`.adaptive.persistence.absolute
import `fun`.adaptive.persistence.append
import `fun`.adaptive.persistence.deleteRecursively
import `fun`.adaptive.persistence.ensure
import `fun`.adaptive.persistence.resolve
import `fun`.adaptive.persistence.write
import `fun`.adaptive.value.embedded.EmbeddedValueServer.Companion.embeddedValueServer
import `fun`.adaptive.value.persistence.FilePersistence
import kotlinx.io.files.Path

class GroveDocCompilation(
    val inPath: Path,
    val mdOutPath: Path,
    val valuesOutPath : Path,
    val baseUrl: String = "https://github.com/spxbhuhb/adaptive/tree/main"
) {

    val inPathAbsolute: String = inPath.absolute().toString().replace('\\', '/')

    val outPathHumanReadable: Path = mdOutPath.resolve("human-readable").ensure()

    val outPathTrainingSeparated: Path = mdOutPath.resolve("separated").ensure()
    val outPathTrainingMerged: Path = mdOutPath.resolve("merged").ensure()

    val outPathTrainingDef: Path = outPathTrainingMerged.resolve("def.md")
    val outPathTrainingQa: Path = outPathTrainingMerged.resolve("qa.md")
    val outPathTrainingGuide: Path = outPathTrainingMerged.resolve("guide.md")

    val valueServer = embeddedValueServer(FilePersistence(valuesOutPath.ensure()))
    val valueWorker = valueServer.serverWorker

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
        outPathHumanReadable.resolve(type + "-" + path.name).write(content)
    }

    fun outputTraining(type: String, path: Path, content: String) {
        val contentWithHeader = "<!-- name: ${path.name} -->\n<!-- type: $type -->\n\n$content\n\n".encodeToByteArray()
        outPathTrainingSeparated.resolve(type + "-" + path.name).write(contentWithHeader)

        when (type) {
            "definition" -> outPathTrainingDef.append(contentWithHeader)
            "guide" -> outPathTrainingGuide.append(contentWithHeader)
            "qa" -> outPathTrainingQa.append(contentWithHeader)
        }
    }

}