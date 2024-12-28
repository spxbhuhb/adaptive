package `fun`.adaptive.resource.codegen

import `fun`.adaptive.resource.DensityQualifier
import `fun`.adaptive.resource.LanguageQualifier
import `fun`.adaptive.resource.Qualifier
import `fun`.adaptive.resource.RegionQualifier
import `fun`.adaptive.resource.ResourceFile
import `fun`.adaptive.resource.ResourceFileSet
import `fun`.adaptive.resource.ResourceTypeQualifier
import `fun`.adaptive.resource.ThemeQualifier
import `fun`.adaptive.utility.DangerousApi
import `fun`.adaptive.utility.deleteRecursively
import `fun`.adaptive.utility.exists
import `fun`.adaptive.utility.syncBySizeAndLastModification
import `fun`.adaptive.utility.walkFiles
import kotlinx.io.files.Path

@DangerousApi("deletes everything in targetPath recursively if sourcePath does not exist")
internal fun copyFilesAndCollectResourceSets(
    sourcePath: Path,
    targetPath: Path,
    resourceSetsByType: Map<ResourceTypeQualifier, MutableMap<String, ResourceFileSet<*>>>,
    errors: MutableList<String>
): Boolean {

    if (! sourcePath.exists()) {
        if (! targetPath.exists()) return false
        targetPath.deleteRecursively()
    }

    val changed = targetPath.syncBySizeAndLastModification(sourcePath)
    if (! changed) return false

    val prefix = targetPath.toString()

    targetPath.walkFiles { mapToResourceFile(prefix, resourceSetsByType, errors, it) }

    return true
}

internal fun mapToResourceFile(
    prefix: String,
    resourceSetsByType: Map<ResourceTypeQualifier, MutableMap<String, ResourceFileSet<*>>>,
    errors: MutableList<String>,
    path: Path
) {

    val relativeDirPath = path.parent.toString().removePrefix(prefix)
    val dirQualifiers = relativeDirPath.split("/").flatMap { it.split("-") }.filter { it.isNotEmpty() }

    val nameAndQualifiers = path.name.substringBeforeLast('.').split('-')
    val fileQualifiers = nameAndQualifiers.drop(1)

    val name = nameAndQualifiers.first()
    val qualifiers = dirQualifiers + fileQualifiers

    val qualifierSet = qualifiers.mapNotNull { mapQualifier(it, errors) }.toSet()
    if (qualifierSet.size != qualifiers.size) return

    val file = ResourceFile(
        "$relativeDirPath/${path.name}",
        qualifierSet
    )

    val type = getType(path, file.qualifiers, errors) ?: return

    val resourceSets = resourceSetsByType[type] !!
    val resourceSet = resourceSets[name]

    if (resourceSet == null) {
        resourceSets[name] = ResourceFileSet(name, type, mutableListOf(file))
        return
    }

    val existing = resourceSet.files.find { it.qualifiers == file.qualifiers }
    if (existing != null) {
        errors += "Resource files with the same qualifier:\n    $path\n    $existing"
        return
    }

    resourceSet.files as MutableList += file
}

internal fun mapQualifier(text: String, errors: MutableList<String>): Qualifier? =
    ResourceTypeQualifier.parse(text)
        ?: LanguageQualifier.parse(text)
        ?: RegionQualifier.parse(text)
        ?: ThemeQualifier.parse(text)
        ?: DensityQualifier.parse(text)
        ?: null.also { errors += "Unknown qualifier: $text" }


internal fun getType(path: Path, qualifiers: Set<Qualifier>, errors: MutableList<String>): ResourceTypeQualifier? {

    val types = qualifiers.filterIsInstance<ResourceTypeQualifier>().distinct()

    if (types.isEmpty()) {
        errors += "Cannot determine resource type for:\n    $path"
        return null
    }

    if (types.size > 1) {
        errors += "Ambiguous resource types ($types) for\n    $path"
        return null
    }

    return types.first()
}