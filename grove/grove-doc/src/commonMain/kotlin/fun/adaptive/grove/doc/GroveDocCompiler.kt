package `fun`.adaptive.grove.doc

import `fun`.adaptive.markdown.compiler.MarkdownCompiler
import `fun`.adaptive.markdown.transform.MarkdownToMarkdownVisitor.Companion.toMarkdown
import `fun`.adaptive.utility.readString
import kotlinx.io.files.Path

class
GroveDocCompiler(
    val compilation: GroveDocCompilation
) {

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
        val transform = MarkdownResolveTransform(compilation, path)
        val outAst = inAst.map { it.transform(transform, null) }
        compilation.output(type, path, outAst.toMarkdown())
    }

}