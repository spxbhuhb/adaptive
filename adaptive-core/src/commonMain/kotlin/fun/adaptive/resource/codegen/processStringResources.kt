package `fun`.adaptive.resource.codegen

import `fun`.adaptive.kotlin.writer.kotlinWriter
import `fun`.adaptive.kotlin.writer.kwFile
import `fun`.adaptive.kotlin.writer.kwModule
import `fun`.adaptive.resource.ResourceFileSet
import `fun`.adaptive.resource.ResourceTypeQualifier
import `fun`.adaptive.resource.avs.AvsWriter
import `fun`.adaptive.resource.codegen.ResourceCompilation.FileAndValues
import `fun`.adaptive.resource.codegen.kotlin.stringResource
import `fun`.adaptive.utility.delete
import `fun`.adaptive.utility.readString
import `fun`.adaptive.utility.resolve
import `fun`.adaptive.utility.uppercaseFirstChar
import `fun`.adaptive.utility.write
import kotlin.String

/**
 * String accessors are structured and complicated. Each string resource set has its own
 * property which does not reference an object but handles string on its own.
 *
 * - The compilation may have more than one string resource sets.
 * - Each should go into its own source file(s).
 * - Set source files should be chunked to avoid problems detailed in [generateUnstructuredAccessors].
 * - Values should be exported into AVS format, sharing indices.
 * - Sources should use the indices of the AVS files.
 */
fun ResourceCompilation.processStringResources() {
    logger?.fine { "Generating string resources" }

    resourceSetsByType[ResourceTypeQualifier.Strings]?.forEach { (_, resourceFileSet) ->
        processStringResources(resourceFileSet)
    }
}

fun ResourceCompilation.processStringResources(resourceFileSet: ResourceFileSet<*>) {

    logger?.apply {
        fine { "Generating string resources for ${resourceFileSet.name}" }
        resourceFileSet.files.forEach {
            fine { "    ${it.path}" }
        }
    }

    val originalReportSize = reports.size

    // load all values from all the files

    val filesAndValues = resourceFileSet.files.map {
        val path = originalResourcesPath.resolve(it.path)
        FileAndValues(
            it,
            mutableMapOf<String, ResourceCompilation.ResourceValue>().also {
                collectValuesFromXml(path, path.readString(), it)
            }
        )
    }

    if (reports.size > originalReportSize) return

    // create the indices, check consistency

    val sortedKeys = indexValueSets(filesAndValues)

    if (reports.size > originalReportSize) return
    check(sortedKeys != null)

    // export the AVS files

    filesAndValues.forEach {
        exportToAvs(it, sortedKeys)
    }

    // generate accessor source code

    generateStringAccessors(
        resourceFileSet,
        sortedKeys
    )
}

/**
 * Check that all value sets contains the same keys and return
 * with the sorted list of keys.
 */
internal fun ResourceCompilation.indexValueSets(
    sets: List<FileAndValues>
): List<String>? {
    if (sets.isEmpty()) return emptyList()

    val sortedKeys = sets.map { it.values.keys.sorted() }

    if (sortedKeys.distinct().size != 1) {
        reportValueProblems(sortedKeys, sets)
        return null
    }

    return sortedKeys.first()
}

internal fun ResourceCompilation.reportValueProblems(
    sortedKeys: List<List<String>>,
    sets: List<FileAndValues>
) {
    sortedKeys.forEachIndexed { index, keys ->
        val otherKeys = sortedKeys.filterIndexed { i, _ -> i != index }.flatten().toSet()
        val missingKeys = otherKeys - keys.toSet()
        val extraKeys = keys.toSet() - otherKeys
        if (missingKeys.isNotEmpty()) {
            compilationError { "Values in ${sets[index].file.path} are missing keys: $missingKeys" }
        }
        if (extraKeys.isNotEmpty()) {
            compilationError { "Values in ${sets[index].file.path} have extra keys: $extraKeys" }
        }
    }
}

fun ResourceCompilation.exportToAvs(
    fileAndValues: FileAndValues,
    sortedKeys: List<String>
) {
    val writer = AvsWriter()
    sortedKeys.forEach { writer += fileAndValues.values[it] !!.value }

    preparedResourcesPath
        .resolve(fileAndValues.file.path.toString().replaceAfterLast('.', "avs"))
        .write(
            writer.pack(),
            overwrite = true
        )

    // delete the copied XML file from the working copy
    preparedResourcesPath
        .resolve(fileAndValues.file.path)
        .delete()
}

fun ResourceCompilation.generateStringAccessors(
    resourceSet: ResourceFileSet<*>,
    sortedKeys: List<String>,
) {

    val chunkSize = 500
    val chunks = sortedKeys.chunked(chunkSize)

    kotlinWriter {
        kwModule {
            chunks.forEachIndexed { chunkIndex, chunk ->
                val accessorObjectName = "$kmpSourceSet${resourceSet.name.uppercaseFirstChar()}StringStore${chunkIndex}"

                kwFile(
                    fileName = accessorObjectName,
                    packageName = packageName
                ) {
                    stringResource(
                        accessorObjectName,
                        resourceSet,
                        chunkIndex * chunkSize,
                        sortedKeys
                    )
                }
            }
        }
    }.also { writer ->
        writer.render()
        writer.modules.first().files.forEach { file ->
            generatedCodePath.resolve(file.name + ".kt").write(file.renderedSource)
        }
    }

}