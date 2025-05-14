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

    /**
     * Look up the code in [ktFiles]. If the code is not found, return null.
     *
     * @param  scheme  The scheme of the code, one of "class", "function" or "property".
     * @param  name    Name of the class, function or property to find.
     * @param  scope   The fully qualified path to the name or the simple name of the class
     *                 if it is unique in the project.
     */
    fun lookupCode(scheme: String, name: String, scope: String?): Path? {
        val lcName = name.lowercase()

        when (scheme) {

            "class" -> {
                if (scope == null) {
                    return ktFiles[lcName]?.firstOrNull()
                } else {
                    return ktFiles[lcName]?.firstOrNull { isInScope(it, scope) }
                }
            }

            "function", "property" -> {

                if (scope == null) {
                    compilation.warn("Missing scope for $scheme $name")
                    return null
                }

                val files = ktFiles[scope.lowercase()]

                if (files == null) {
                    compilation.warn("Cannot find scope $scope for $scheme $name")
                    return null
                }

                if (files.size != 1) {
                    compilation.warn("Multiple scopes found for $scheme $name")
                    return null
                }

                return files.first()
            }

            else -> return null
        }
    }

    private fun isInScope(path: Path, scope: String): Boolean {
        val normalizedScope = scope.replace('/', '.')

        val normalizedPath = path.toString().removeSuffix(path.name)
            .replace('/', '.').replace('\\', '.').trim('.')

        return normalizedPath.endsWith(normalizedScope)
    }

}

