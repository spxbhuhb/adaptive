package `fun`.adaptive.grove.doc

import `fun`.adaptive.markdown.compiler.MarkdownCompiler
import `fun`.adaptive.markdown.transform.MarkdownToMarkdownVisitor.Companion.toMarkdown
import `fun`.adaptive.utility.readString
import kotlinx.io.files.Path

class GroveDocCompiler(
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

        process(fileCollector.definitions)
        process(fileCollector.guides)

        for (path in fileCollector.uncategorized) {
            process(path)
        }
    }

    fun process(collection: MutableMap<String, MutableList<Path>>) {
        for ((_, paths) in collection) {
            paths.forEach(::process)
        }
    }

    fun process(path: Path) {
        val inAst = MarkdownCompiler.ast(path.readString())
        val transform = MarkdownResolveTransform(compilation, path)
        val outAst = inAst.map { it.transform(transform, null) }
        compilation.output(path.name, outAst.toMarkdown())
    }

}