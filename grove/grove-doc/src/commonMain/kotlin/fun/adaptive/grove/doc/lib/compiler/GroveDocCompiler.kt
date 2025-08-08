package `fun`.adaptive.grove.doc.lib.compiler

import `fun`.adaptive.grove.doc.model.*
import `fun`.adaptive.markdown.compiler.MarkdownCompiler
import `fun`.adaptive.markdown.model.MarkdownHeader
import `fun`.adaptive.markdown.model.MarkdownInline
import `fun`.adaptive.markdown.transform.MarkdownToMarkdownVisitor.Companion.toMarkdown
import `fun`.adaptive.persistence.absolute
import `fun`.adaptive.persistence.readString
import `fun`.adaptive.utility.UUID.Companion.uuid4
import `fun`.adaptive.value.AvMarker
import `fun`.adaptive.value.AvStatus
import `fun`.adaptive.value.AvValue
import kotlinx.coroutines.channels.Channel
import kotlin.time.Instant
import kotlinx.io.files.Path

class
GroveDocCompiler(
    val compilation : GroveDocCompilation
) {

    internal val fileCollector
        get() = compilation.fileCollector

    val values
        get() = compilation.values

    val notifications
        get() = compilation.notifications

    val subprojects = mutableListOf<Subproject>()
    val subprojectValues = mutableListOf<AvValue<GroveDocSpec>>()
    val docTreeNodes = mutableMapOf<String, GroveDocValue>()

    // --------------------------------------------------------------------------------
    // Initialization
    // --------------------------------------------------------------------------------

    /**
     * Re-compiles the documentation and updates values in the value store.
     */
    fun compile() {
        fileCollector.collectFiles(compilation.inPath)
        fileCollector.reportCollisions()
        subprojects += fileCollector.collectSubprojectsFromSettings()

        collectAndAddSubprojects()

        processExamples(compilation.exampleGroups) // transformed examples used by Markdown transform as well
        addExampleGroupValues(compilation.exampleGroups)

        processMarkdownGroup("definition", fileCollector.definitions)
        processMarkdownGroup("guide", fileCollector.guides)

        for (path in fileCollector.uncategorized) {
            processMarkdown("uncategorized", path)
        }
    }

    /**
     * Calls [compile] for an initial re-compilation and then updates the
     * compilation when an event arrives.
     *
     * Close the channel to stop.
     */
    suspend fun continuousCompile(channel : Channel<GroveDocFileEvent>) {

        compile()

        for (fileEvent in channel) {

            val path = Path(fileEvent.path)
            val example = fileEvent.path.endsWith(".kt")
            if (example) continue // TODO handle examples in continuous compilation

            val group = when {
                fileEvent.path.contains("/definitions/") -> groveDocDomain.definition
                fileEvent.path.contains("/guides/") -> groveDocDomain.guide
                else -> continue
            }

            val collection = when (group) {
                groveDocDomain.definition -> fileCollector.definitions
                groveDocDomain.guide -> fileCollector.guides
                else -> continue
            }

            when (fileEvent.type) {
                GroveDocFileEventType.Create -> {
                    compilation.logger.info { "Adding file: $path" }
                    fileCollector.putFile(collection, path.name, path)
                    processMarkdown(group, path)
                }

                GroveDocFileEventType.Modify -> {
                    // FIXME handle file rename
                    compilation.logger.info { "Updating file: $path" }
                    processMarkdown(group, path)
                }

                GroveDocFileEventType.Delete -> {
                    // FIXME should delete the output files as well
                    compilation.logger.info { "Removing file: $path" }
                    collection.remove(compilation.normalizedName(path.name))
                    values.executeOutOfBand {
                        val relativePath = path.relative()
                        val existing = get<GroveDocSpec>(group).firstOrNull { it.spec.repoPath == relativePath }
                        if (existing != null) {
                            this -= existing
                        }
                    }
                }
            }
        }
    }

    // --------------------------------------------------------------------------------
    // Subprojects
    // --------------------------------------------------------------------------------

    fun collectAndAddSubprojects() {
        val knownSubProjects = values.get<GroveDocSpec>(groveDocDomain.subProject)
        for (subproject in subprojects) {
            val existing = knownSubProjects.firstOrNull { it.name == subproject.name }
            if (existing != null) {
                registerSubProject(existing)
            } else {
                addSubProject(subproject.name, subproject.relativePath)
            }
        }
    }

    private fun registerSubProject(existing : AvValue<GroveDocSpec>) {
        values.executeOutOfBand {
            subprojectValues += existing
            getTreeChildren<GroveDocSpec>(groveDocDomain.treeDef, existing.uuid).forEach {
                docTreeNodes[it.friendlyId !!] = it
            }
        }
    }

    fun addSubProject(
        name : String,
        relativePath : String
    ) {
        val subProjectId = uuid4<AvValue<*>>()
        val groups = listOf("definitions", "guides", "internals")

        values.executeOutOfBand {

            addTreeNode(groveDocDomain.treeDef) {
                AvValue(
                    uuid = subProjectId,
                    friendlyId = name,
                    name = name,
                    markersOrNull = setOf(groveDocDomain.subProject),
                    spec = GroveDocSpec(
                        repoPath = relativePath,
                        lastUpdate = null,
                        content = ""
                    )
                )
            }.also {
                subprojectValues += it
            }

            groups.forEach { group ->
                addTreeNode(groveDocDomain.treeDef, subProjectId) {
                    AvValue(
                        friendlyId = "$name/$group",
                        name = group,
                        markersOrNull = setOf(groveDocDomain.group),
                        spec = GroveDocSpec(
                            repoPath = "$relativePath/$group",
                            null,
                            ""
                        )
                    )
                }.also {
                    docTreeNodes[it.friendlyId !!] = it
                }
            }
        }
    }


    // --------------------------------------------------------------------------------
    // Markdown files
    // --------------------------------------------------------------------------------

    class ContentAndHeader(
        val content : String,
        val lastUpdate : Instant?,
        val status : Set<AvStatus>?,
        val markers : Set<AvMarker>
    )

    fun processMarkdownGroup(group : String, collection : MutableMap<String, MutableList<Path>>) {

        val allPaths = collection.values.flatten()

        val bySubproject : Map<String, List<Path>> = allPaths.groupBy { path ->
            val matched = subprojectValues.firstOrNull { sp ->
                path.toString().contains("/${sp.name}/")
            }
            matched?.name ?: ""
        }

        for ((_, paths) in bySubproject.toList().sortedBy { it.first }) {
            paths.sortedBy { p -> p.toString() }.forEach { path -> processMarkdown(group, path) }
        }
    }

    fun processMarkdown(group : String, path : Path) {
        val contentAndHeader = readAndProcessHeader(path)

        val inAst = MarkdownCompiler.ast(contentAndHeader.content)

        val trainingTransform = MarkdownResolveTransform(compilation, path, training = true)
        val trainingOutAst = inAst
            .map { it.transform(trainingTransform, null) }
            .takeWhile { element ->
                ! (
                    element is MarkdownHeader &&
                        element.children.firstOrNull()?.let { it is MarkdownInline && it.text.contains("see also", ignoreCase = true) } == true
                    )
            }

        compilation.outputTraining(subprojects, group, path, trainingOutAst.toMarkdown())

        val humanReadableTransform = MarkdownResolveTransform(compilation, path, training = false)
        val humanReadableOutAst = inAst.map { it.transform(humanReadableTransform, null) }

        val humanReadable = humanReadableOutAst.toMarkdown()
        compilation.outputHumanReadable(group, path, humanReadable)

        val withoutTitle = if (humanReadableOutAst.firstOrNull() is MarkdownHeader) {
            humanReadableOutAst.subList(1, humanReadableOutAst.size).toMarkdown()
        } else {
            humanReadable
        }

        upsertMarkdownTreeValue(group, path, contentAndHeader, withoutTitle)
    }

    private fun upsertMarkdownTreeValue(
        group : String,
        path : Path,
        contentAndHeader : ContentAndHeader,
        contentWithoutTitle : String,
    ) {
        for (subproject in subprojectValues) {

            if (! path.toString().contains("/${subproject.name}/")) continue

            val groupValue = docTreeNodes["${subproject.name}/${group}s"] ?: return // keep old behavior (break)
            val relativePath = path.relative()

            values.executeOutOfBand {
                val existing = get<GroveDocSpec>(group).firstOrNull { it.spec.repoPath == relativePath }

                val spec = GroveDocSpec(
                    repoPath = relativePath,
                    lastUpdate = contentAndHeader.lastUpdate,
                    content = contentWithoutTitle
                )

                if (existing != null) {
                    this += existing.copy(
                        name = path.name.substringBeforeLast('.'),
                        statusOrNull = contentAndHeader.status,
                        markersOrNull = existing.markers + contentAndHeader.markers,
                        spec = spec
                    )
                } else {
                    addTreeNode(groveDocDomain.treeDef, groupValue.uuid) {
                        AvValue(
                            name = path.name.substringBeforeLast('.'),
                            statusOrNull = contentAndHeader.status,
                            markersOrNull = setOf(group) + contentAndHeader.markers,
                            spec = spec
                        )
                    }
                }
            }

            break
        }
    }

    val headerRegex = """(---([\s\S]*?)---)""".toRegex()
    val lastChangeRegex = """\n\s*lastChange: (.*)\s*\n""".toRegex()
    val statusRegex = """\n\s*status: (.*)\s*\n""".toRegex()
    val markerRegex = """\n\s*markers: (.*)\s*\n""".toRegex()

    fun readAndProcessHeader(path : Path) : ContentAndHeader {
        val content = path.readString()

        if (! content.startsWith("---")) {
            return ContentAndHeader(content, null, null, emptySet())
        }

        val headerMatch = headerRegex.find(content)
        if (headerMatch == null || headerMatch.range.first != 0) {
            return ContentAndHeader(content, null, null, emptySet())
        }

        val headerText = headerMatch.groupValues[2]
        val actualContent = content.substring(headerMatch.range.last + 1)

        val lastChange = try {
            lastChangeRegex.find(headerText)?.groupValues[1]?.let { Instant.parse(it) }
        } catch (e : Exception) {
            compilation.warn("Failed to parse lastChange date in ${path.absolute()}", listOf(path))
            null
        }

        val status = statusRegex.find(headerText)?.groupValues?.get(1)?.split(",")?.toSet()

        val markers = markerRegex.find(headerText)?.groupValues?.get(1)?.split(",")?.toSet() ?: emptySet()

        return ContentAndHeader(actualContent, lastChange, status, markers)
    }

    // --------------------------------------------------------------------------------
    // Examples
    // --------------------------------------------------------------------------------

    fun processExamples(out : MutableMap<String, List<GroveDocExample>>) {
        for ((name, items) in compilation.fileCollector.examples) {
            out[name] = items.map { processExample(it) }.sortedBy { it.repoPath } // this sorting is not perfect, but well...
        }
    }

    fun processExample(path : Path) : GroveDocExample {
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
                    .joinToString("\n") { line -> line.trim().removePrefix("*").trim() }
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
            repoPath = path.relative(),
            fragmentKey = fragmentKey,
            fullCode = fullCode,
            exampleCode = exampleCode,
        )
    }

    fun addExampleGroupValues(exampleMap : Map<String, List<GroveDocExample>>) {
        for ((name, examples) in exampleMap) {
            compilation.values.executeOutOfBand {
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

    fun Path.relative() =
        absolute().toString().replace('\\', '/').removePrefix(compilation.inPathAbsolute)


}
