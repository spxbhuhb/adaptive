package `fun`.adaptive.grove.doc.lib.compiler

import `fun`.adaptive.persistence.isDirectory
import `fun`.adaptive.persistence.list
import `fun`.adaptive.persistence.readString
import `fun`.adaptive.persistence.resolve
import `fun`.adaptive.persistence.exists
import kotlinx.io.files.Path
import kotlin.collections.iterator

internal class FileCollector(
    val compilation : GroveDocCompilation
) {

    val excluded = listOf("build", "deprecated")

    // file name -> paths to with the given file name (directories may differ)
    val ktFiles = mutableMapOf<String, MutableList<Path>>()

    // key is the name of the example group
    val examples = mutableMapOf<String, MutableList<Path>>()

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

            name.endsWith(".kt") -> {
                putFile(ktFiles, name, path, normalize = false)

                // example file name structure: <order>_<group>_<name>_example.kt
                if (name.endsWith("_example.kt")) {
                    val parts = name.split("_")
                    if (parts.size != 4) {
                        compilation.warn("Invalid example name: $name")
                    } else {
                        val group = examples.getOrPut(parts[1]) { mutableListOf() }
                        group += path
                    }
                }
            }
        }
    }

    fun putFile(
        collection : MutableMap<String, MutableList<Path>>,
        name : String,
        path : Path,
        normalize : Boolean = true
    ) {
        val putName = if (normalize) compilation.normalizedName(name) else name.substringBeforeLast('.')
        collection.getOrPut(putName) { mutableListOf() }.add(path)
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
        when (scheme) {

            "class", "example" -> {
                if (scope == null) {
                    return ktFiles[name]?.firstOrNull()
                } else {
                    val files = ktFiles[scope] ?: ktFiles[name]
                    if (files?.size == 1) return files.first()
                    return files?.firstOrNull { isInScope(it, scope) }
                }
            }

            "function", "property" -> {

                if (scope == null) {
                    val files = ktFiles[name]
                    if (files == null) {
                        compilation.warn("Missing scope for $scheme $name")
                        return null
                    }
                    if (files.size != 1) {
                        compilation.warn("Multiple scopes found for $scheme $name")
                        return null
                    }
                    return files.first()
                }

                val files = ktFiles[scope]

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

    fun lookupDef(name: String): Path? =
        definitions[name]?.firstOrNull()

    /**
     * Reads settings.gradle.kts and collects included builds.
     */
    fun collectSubprojectsFromSettings(
        settingsPath : Path = compilation.inPath.resolve("settings.gradle.kts")
    ): List<Subproject> {
        if (! settingsPath.exists()) return emptyList()

        val content = settingsPath.readString()
        val regex = Regex("includeBuild\\(([^)]+)\\)")

        val results = mutableListOf<Subproject>()

        for (match in regex.findAll(content)) {
            val rel = match.groups[1]?.value?.trim()?.trim('\'', '"') ?: continue
            val name = rel.trimEnd('/', '\\').substringAfterLast('/').substringAfterLast('\\')
            val resolved = compilation.inPath.resolve(rel)
            results += Subproject(name = name, relativePath = rel, path = resolved)
        }

        return results.distinctBy { it.name to it.relativePath }
    }
}

