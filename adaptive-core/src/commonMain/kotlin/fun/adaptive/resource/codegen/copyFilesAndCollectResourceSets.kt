package `fun`.adaptive.resource.codegen

import `fun`.adaptive.resource.*
import `fun`.adaptive.utility.*
import kotlinx.io.files.Path

@DangerousApi("deletes everything in targetPath recursively if sourcePath does not exist")
fun ResourceCompilation.copyFilesAndCollectResourceSets(): Boolean {

    if (! sourcePath.exists()) {
        if (! preparedResourcesPath.exists()) return false
        preparedResourcesPath.deleteRecursively()
    }

    val changed = preparedResourcesPath.syncBySizeAndLastModification(sourcePath)
    if (! changed) return false

    val prefix = preparedResourcesPath.toString()

    preparedResourcesPath.walkFiles { mapToResourceFile(prefix, it) }

    return true
}

fun ResourceCompilation.mapToResourceFile(prefix: String, path: Path) {

    val relativeDirPath = path.parent.toString().removePrefix(prefix)
    val dirQualifiers = relativeDirPath.split("/").flatMap { it.split("-") }.filter { it.isNotEmpty() }

    val nameAndQualifiers = path.name.substringBeforeLast('.').split('-')
    val fileQualifiers = nameAndQualifiers.drop(1)

    val name = nameAndQualifiers.first()
    val qualifiers = dirQualifiers + fileQualifiers

    val qualifierSet = qualifiers.mapNotNull { mapQualifier(it) }.toSet()
    if (qualifierSet.size != qualifiers.size) return

    val file = ResourceFile(
        "$relativeDirPath/${path.name}", // this will be replaced with .avs for structured resources
        qualifierSet
    )

    val type = getType(path, file.qualifiers) ?: return

    val resourceSets = resourceSetsByType[type] !!
    val resourceSet = resourceSets[name]

    if (resourceSet == null) {
        resourceSets[name] = ResourceFileSet(name, type, mutableListOf(file))
        return
    }

    val existing = resourceSet.files.find { it.qualifiers == file.qualifiers }
    if (existing != null) {
        compilationError { "Resource files with the same qualifier:\n    $path\n    $existing" }
        return
    }

    resourceSet.files as MutableList += file
}

fun ResourceCompilation.mapQualifier(text: String): Qualifier? =
    ResourceTypeQualifier.parse(text)
        ?: LanguageQualifier.parse(text)
        ?: RegionQualifier.parse(text)
        ?: ThemeQualifier.parse(text)
        ?: DensityQualifier.parse(text)
        ?: null.also { compilationError { "Unknown qualifier: $text" } }


fun ResourceCompilation.getType(path: Path, qualifiers: Set<Qualifier>): ResourceTypeQualifier? {

    val types = qualifiers.filterIsInstance<ResourceTypeQualifier>().distinct()

    if (types.isEmpty()) {
        compilationError { "Cannot determine resource type for:\n    $path" }
        return null
    }

    if (types.size > 1) {
        compilationError { "Ambiguous resource types ($types) for\n    $path" }
        return null
    }

    return types.first()
}