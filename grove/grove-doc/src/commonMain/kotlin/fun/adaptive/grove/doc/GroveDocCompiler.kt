package `fun`.adaptive.grove.doc

import `fun`.adaptive.markdown.compiler.MarkdownCompiler
import `fun`.adaptive.markdown.transform.MarkdownToMarkdownVisitor.Companion.toMarkdown
import `fun`.adaptive.utility.absolute
import `fun`.adaptive.utility.readString
import kotlinx.io.files.Path

class
GroveDocCompiler(
    val compilation: GroveDocCompilation
) {

    init {
        compilation.inPathAbsolute = compilation.inPath.absolute().toString().replace('\\', '/')
    }

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

        process("definition", fileCollector.definitions)
        process("guide", fileCollector.guides)

        for (path in fileCollector.uncategorized) {
            process("uncategorized", path)
        }
    }

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

        compilation.outputHumanReadable(type, path, humanReadableOutAst.toMarkdown())
    }

}