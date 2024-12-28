package `fun`.adaptive.resource.codegen

import `fun`.adaptive.resource.ResourceFileSet
import `fun`.adaptive.resource.ResourceTypeQualifier
import `fun`.adaptive.utility.DangerousApi
import kotlinx.io.files.Path

@DangerousApi("deletes everything in targetPath recursively if sourcePath does not exist")
fun processResources(
    sourcePath: Path,
    packageName: String,
    kmpSourceSet: String,
    generatedCodePath : Path,
    preparedResourcesPath: Path
) {
    val resourceSetsByType = ResourceTypeQualifier.entries
        .map { it to mutableMapOf<String, ResourceFileSet<*>>() }
        .associate { it }

    val errors = mutableListOf<String>()

    copyFilesAndCollectResourceSets(sourcePath, preparedResourcesPath, resourceSetsByType, errors)

    if (errors.isNotEmpty()) {
        throw IllegalStateException(
            "Errors occurred during resource processing:\n" +
                errors.joinToString("\n")
        )
    }

    ResourceTypeQualifier.entries.forEach { type ->
        if (type.isUnstructured) {
            generateUnstructuredAccessors(
                generatedCodePath,
                packageName,
                kmpSourceSet,
                type,
                resourceSetsByType
            )
        }
    }

    generateStringAccessors(resourceSetsByType)
}