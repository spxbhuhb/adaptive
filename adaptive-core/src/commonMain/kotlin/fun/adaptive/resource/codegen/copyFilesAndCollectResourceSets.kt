package `fun`.adaptive.resource.codegen

import `fun`.adaptive.resource.*
import `fun`.adaptive.utility.*
import kotlinx.io.files.Path

@DangerousApi("deletes everything in target path recursively")
fun ResourceCompilation.copyFilesAndCollectResourceSets(): Boolean {

    logger?.fine { "Collecting files and resources" }

    if (! originalResourcesPath.exists()) {
        if (! preparedResourcesPath.exists()) {
            logger?.fine { "Original resource path does not exits: $originalResourcesPath" }
            return false
        }
        preparedResourcesPath.deleteRecursively()
    }

    val changed = preparedResourcesPath.syncBySizeAndLastModification(originalResourcesPath, remove = true)
    if (! changed) {
        logger?.fine { "No changes detected in original resources: $originalResourcesPath" }
        return false
    }

    val prefix = preparedResourcesPath.toString()

    // TODO Figure out if optimization of code generation is worth it
    generatedCodePath.deleteRecursively()

    preparedResourcesPath.walkFiles { mapToResourceFile(prefix, it) }

    return true
}

fun ResourceCompilation.mapToResourceFile(prefix: String, path: Path) {

    logger?.fine { "Mapping file: $path" }

    val relativeDirPath = path.parent.toString().removePrefix(prefix)
    val dirQualifiers = relativeDirPath.split("/").flatMap { it.split("-") }.filter { it.isNotEmpty() }

    val name : String
    val fileQualifiers : List<String>

    if (withFileQualifiers) {
        val nameAndQualifiers = path.name.substringBeforeLast('.').split('-')
        fileQualifiers = nameAndQualifiers.drop(1)
        name = nameAndQualifiers.first()
    } else {
        fileQualifiers = emptyList()
        name = path.name.substringBeforeLast('.').asUnderscoredIdentifier()
    }

    val qualifiers = dirQualifiers + fileQualifiers

    val qualifierSet = qualifiers.mapNotNull { mapQualifier(it) }.toSet()
    if (qualifierSet.size != qualifiers.size) {
        logger?.fine { "qualifier mapping difference for: $path"}
        logger?.fine { "    qualifiers: $qualifiers"}
        logger?.fine { "    qualifierSet: $qualifierSet"}
        return
    }

    // structured processing will replace path with .avs
    // Kotlin code generation will add the package name to the path
    // (we cannot add package name here because then we can't find the file easily)

    val file = ResourceFile(
        "$relativeDirPath/${path.name}",
        qualifierSet
    )

    val type = getType(path, file.qualifiers)

    if (type == null) {
        logger?.fine { "cannot determine file type for: $path" }
        return
    }

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
        if (withFileDefault) {
            // invalid directory names won't get to this point as `mapQualifier` drops them
            // so qualifiers are all valid at this point, only type is missing
            return ResourceTypeQualifier.File
        } else {
            compilationError { "Cannot determine resource type for:\n    $path" }
            return null
        }
    }

    if (types.size > 1) {
        compilationError { "Ambiguous resource types ($types) for\n    $path" }
        return null
    }

    return types.first()
}