package `fun`.adaptive.grove.doc.lib.compiler

import `fun`.adaptive.grove.doc.model.avDomain
import `fun`.adaptive.markdown.compiler.MarkdownCompiler
import `fun`.adaptive.markdown.model.MarkdownHeader
import `fun`.adaptive.markdown.transform.MarkdownToMarkdownVisitor.Companion.toMarkdown
import `fun`.adaptive.persistence.readString
import `fun`.adaptive.value.AvValue
import kotlinx.io.files.Path

class
GroveDocCompiler(
    val compilation: GroveDocCompilation
) {

    val notifications
        get() = compilation.notifications

    val subprojects = mutableListOf<String>()
    val docTreeNodes = mutableMapOf<String, AvValue<String>>()

    fun compile() {
        collect()
        process()
    }

    fun collect() {
        val fileCollector = compilation.fileCollector

        fileCollector.collectFiles(compilation.inPath)
        fileCollector.reportCollisions()
    }

    fun process() {
        val fileCollector = compilation.fileCollector

        collectSubprojects()
        buildDocTreeTops()

        process("definition", fileCollector.definitions)
        process("guide", fileCollector.guides)
        process("qa", fileCollector.qa)

        for (path in fileCollector.uncategorized) {
            process("uncategorized", path)
        }
    }

    fun buildDocTreeTops() {

        subprojects.sort()
        val groups = listOf("definitions", "guides")

        compilation.valueWorker.executeOutOfBand {

            for (project in subprojects) {

                val projectNode = addTreeNode(avDomain.treeDef) {
                    AvValue(
                        friendlyId = project,
                        name = project,
                        markersOrNull = setOf(avDomain.project),
                        spec = ""
                    )
                }

                groups.forEach { group ->
                    addTreeNode(avDomain.treeDef, projectNode.uuid) {
                        AvValue(
                            friendlyId = "$project/$group",
                            name = group,
                            markersOrNull = setOf(avDomain.group),
                            spec = ""
                        )
                    }.also {
                        docTreeNodes[it.friendlyId!!] = it
                    }
                }

            }
        }
    }

    fun collectSubprojects() {
        for (paths in compilation.fileCollector.definitions.values) {
            for (path in paths) {
                if (isSubprojectDefinition(path.readString())) {
                    subprojects += path.name.substringBeforeLast('.')
                }
            }
        }
    }

    fun isSubprojectDefinition(def : String) =
        // FIXME hackish finding of a subproject
        // maybe add a header to the markdown files with tags?
        def.contains("[subproject](def://)")

    fun process(type: String, collection: MutableMap<String, MutableList<Path>>) {
        for ((_, paths) in collection) {
            paths.forEach { process(type, it) }
        }
    }

    fun process(type: String, path: Path) {
        val inAst = MarkdownCompiler.ast(path.readString())

        val trainingTransform = MarkdownResolveTransform(compilation, path, training = true)
        val trainingOutAst = inAst.map { it.transform(trainingTransform, null) }

        compilation.outputTraining(type, path, trainingOutAst.toMarkdown())

        val humanReadableTransform = MarkdownResolveTransform(compilation, path, training = false)
        val humanReadableOutAst = inAst.map { it.transform(humanReadableTransform, null) }

        val humanReadable = humanReadableOutAst.toMarkdown()
        compilation.outputHumanReadable(type, path, humanReadable)

        val withoutTitle = if (humanReadableOutAst.firstOrNull() is MarkdownHeader) {
            humanReadableOutAst.subList(1, humanReadableOutAst.size).toMarkdown()
        } else {
            humanReadable
        }

        for (subproject in subprojects) {
            if (path.toString().contains("/$subproject/")) {
                val node = docTreeNodes["$subproject/${type}s"] ?: break // FIXME really hackish categorization of docs

                compilation.valueWorker.executeOutOfBand {
                    addTreeNode(avDomain.treeDef, node.uuid) {
                        AvValue(
                            name = path.name.substringBeforeLast('.'),
                            spec = withoutTitle
                        )
                    }
                }
            }
        }
    }

}