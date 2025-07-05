package `fun`.adaptive.grove.doc.lib.compiler

import `fun`.adaptive.grove.doc.model.GroveDocExample
import `fun`.adaptive.grove.doc.model.GroveDocExampleGroupSpec
import `fun`.adaptive.grove.doc.model.GroveDocSpec
import `fun`.adaptive.grove.doc.model.GroveDocValue
import `fun`.adaptive.grove.doc.model.groveDocDomain
import `fun`.adaptive.markdown.compiler.MarkdownCompiler
import `fun`.adaptive.markdown.model.MarkdownHeader
import `fun`.adaptive.markdown.transform.MarkdownToMarkdownVisitor.Companion.toMarkdown
import `fun`.adaptive.persistence.absolute
import `fun`.adaptive.persistence.readString
import `fun`.adaptive.value.AvMarker
import `fun`.adaptive.value.AvValue
import kotlinx.datetime.Instant
import kotlinx.io.files.Path

class
GroveDocCompiler(
    val compilation: GroveDocCompilation
) {

    val notifications
        get() = compilation.notifications

    val subprojects = mutableListOf<String>()
    val docTreeNodes = mutableMapOf<String, GroveDocValue>()

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

        transformExamples(compilation.exampleGroups) // transformed examples used by Markdown transform as well
        addExampleGroupValues(compilation.exampleGroups)

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

                val projectNode = addTreeNode(groveDocDomain.treeDef) {
                    AvValue(
                        friendlyId = project,
                        name = project,
                        markersOrNull = setOf(groveDocDomain.project),
                        spec = GroveDocSpec(null, "")
                    )
                }

                groups.forEach { group ->
                    addTreeNode(groveDocDomain.treeDef, projectNode.uuid) {
                        AvValue(
                            friendlyId = "$project/$group",
                            name = group,
                            markersOrNull = setOf(groveDocDomain.group),
                            spec = GroveDocSpec(null, "")
                        )
                    }.also {
                        docTreeNodes[it.friendlyId !!] = it
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

    fun isSubprojectDefinition(def: String) =
    // FIXME hackish finding of a subproject
        // maybe add a header to the markdown files with tags?
        def.contains("[subproject](def://)")

    fun process(type: String, collection: MutableMap<String, MutableList<Path>>) {
        for ((_, paths) in collection) {
            paths.forEach { process(type, it) }
        }
    }

    class ContentAndHeader(
        val content: String,
        val lastUpdate : Instant?,
        val markers : Set<AvMarker>
    )

    fun process(type: String, path: Path) {
        val contentAndHeader = readAndProcessHeader(path)

        val inAst = MarkdownCompiler.ast(contentAndHeader.content)

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
                    addTreeNode(groveDocDomain.treeDef, node.uuid) {
                        AvValue(
                            name = path.name.substringBeforeLast('.'),
                            markersOrNull = setOf(type) + contentAndHeader.markers,
                            spec = GroveDocSpec(
                                lastUpdate = contentAndHeader.lastUpdate,
                                content = withoutTitle
                            )
                        )
                    }
                }
            }
        }
    }

    val headerRegex = """(---(\n.*\n)---)""".toRegex()
    val lastChangeRegex = """\n\s*lastChange: (.*)\s*\n""".toRegex()
    val markerRegex = """\n\s*markers: (.*)\s*\n""".toRegex()

    fun readAndProcessHeader(path: Path): ContentAndHeader {
        val content = path.readString()

        if (!content.startsWith("---")) {
            return ContentAndHeader(content, null, emptySet())
        }

        val headerMatch = headerRegex.find(content)
        if (headerMatch == null || headerMatch.range.first != 0) {
            return ContentAndHeader(content, null, emptySet())
        }

        val headerText = headerMatch.groupValues[2]
        val actualContent = content.substring(headerMatch.range.last + 1)

        val lastChange = try {
            lastChangeRegex.find(headerText)?.groupValues[1]?.let { Instant.parse(it) }
        } catch (e: Exception) {
            compilation.warn("Failed to parse lastChange date in ${path.absolute()}", listOf(path))
            null
        }

        val markers = markerRegex.find(headerText)?.groupValues?.get(1)?.split(",")?.toSet() ?: emptySet()

        return ContentAndHeader(actualContent, lastChange, markers)

    }

    fun transformExamples(out : MutableMap<String, List<GroveDocExample>>) {
        for ((name, items) in compilation.fileCollector.examples) {
            out[name] = items.map { getExample(it) }.sortedBy { it.repoPath } // this sorting is not perfect, but well...
        }
    }

    fun getExample(path: Path): GroveDocExample {
        val fullCode = path.readString()

        // Extract documentation comment
        val docRegex = """/\*\*([\s\S]*?)\*/""".toRegex()
        val docMatch = docRegex.find(fullCode)

        // Default values in case extraction fails
        var name = path.name.substringBeforeLast('.')
        var explanation = ""
        var exampleCode = fullCode
        var fragmentKey = ""

        docMatch?.let { match ->
            val docContent = match.groupValues[1].trim()

            // Extract name from markdown header
            val nameRegex = """#\s+(.+)""".toRegex()
            val nameMatch = nameRegex.find(docContent)
            nameMatch?.let {
                name = it.groupValues[1].trim()
            }

            // Extract explanation (everything after the header)
            val explanationRegex = """#\s+.+\n([\s\S]*)""".toRegex()
            val explanationMatch = explanationRegex.find(docContent)
            explanationMatch?.let {
                explanation = it.groupValues[1].trim().lines()
                    .map { line -> line.trim().removePrefix("*").trim() }
                    .filter { line -> line.isNotEmpty() }
                    .joinToString("\n")
            }

            // Extract example code (everything after the documentation comment)
            val codeRegex = """\*/\s*([\s\S]*)""".toRegex()
            val codeMatch = codeRegex.find(fullCode)
            codeMatch?.let {
                exampleCode = it.groupValues[1].trim()

                // Extract function name from the code after the documentation
                val functionRegex = """fun\s+(\w+)""".toRegex()
                val functionMatch = functionRegex.find(exampleCode)
                functionMatch?.let { funcMatch ->
                    fragmentKey = funcMatch.groupValues[1].trim()
                }
            }
        }

        return GroveDocExample(
            name = name,
            explanation = explanation,
            repoPath = path.absolute().toString().replace('\\', '/').removePrefix(compilation.inPathAbsolute),
            fragmentKey = fragmentKey,
            fullCode = fullCode,
            exampleCode = exampleCode,
        )
    }

    fun addExampleGroupValues(exampleMap: Map<String, List<GroveDocExample>>) {
        for ((name, examples) in exampleMap) {
            compilation.valueWorker.executeOutOfBand {
                addValue {
                    AvValue(
                        name = "Example group: $name",
                        markersOrNull = setOf(groveDocDomain.exampleGroup + ":" + name),
                        spec = GroveDocExampleGroupSpec(
                            examples = examples
                        )
                    )
                }
            }
        }
    }


}
