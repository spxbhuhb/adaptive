package `fun`.adaptive.grove.doc

import `fun`.adaptive.utility.isDirectory
import `fun`.adaptive.utility.list
import kotlinx.io.files.Path

internal class FileCollector(
    val compilation : GroveDocCompilation
) {

    val excluded = listOf("build", "deprecated")

    val ktFiles = mutableMapOf<String, MutableList<Path>>()

    val definitions = mutableMapOf<String, MutableList<Path>>()
    val guides = mutableMapOf<String, MutableList<Path>>()
    val uncategorized = mutableListOf<Path>()

    var inDefinitions = false
    var inGuides = false

    fun collectFiles(root: Path) {
        for (path in root.list()) {
            val name = path.name
            if (name.startsWith('.') || name in excluded) continue

            if (path.isDirectory) {
                collectFromDirectory(name, path)
                continue
            }

            registerFile(name, path)
        }
    }

    private fun collectFromDirectory(name : String, path : Path) {
        when (name) {
            "definitions" -> {
                inDefinitions = true
                collectFiles(path)
                inDefinitions = false
            }
            "guides" -> {
                inGuides = true
                collectFiles(path)
                inGuides = false
            }
            else -> collectFiles(path)
        }
    }

    private fun registerFile(name: String, path: Path) {
        when {
            name.endsWith(".md") -> when {
                inDefinitions -> putFile(definitions, name, path)
                inGuides -> putFile(guides, name, path)
                //else -> uncategorized.add(path)
            }
            name.endsWith(".kt") -> putFile(ktFiles, name, path)
        }
    }

    private fun putFile(collection : MutableMap<String, MutableList<Path>>, name : String, path : Path) {
        collection.getOrPut(compilation.normalizedName(name)) { mutableListOf() }.add(path)
    }


    fun reportCollisions() {
        for ((key, value) in definitions) {
            checkCollision(key, value)
        }
        for ((key, value) in guides) {
            checkCollision(key, value)
        }
    }

    fun checkCollision(key : String, value : List<Path>) {
        if (value.size != 1) {
            compilation.warn("Markdown file name collision on $key", value)
        }
    }

}