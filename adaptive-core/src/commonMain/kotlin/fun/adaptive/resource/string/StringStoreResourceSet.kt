package `fun`.adaptive.resource.string

import `fun`.adaptive.resource.ResourceEnvironment
import `fun`.adaptive.resource.ResourceReader
import `fun`.adaptive.resource.avs.AvsReader
import `fun`.adaptive.resource.defaultResourceEnvironment
import `fun`.adaptive.resource.defaultResourceReader
import `fun`.adaptive.resource.ResourceFileSet
import `fun`.adaptive.resource.ResourceTypeQualifier
import `fun`.adaptive.resource.file.FileResource

class StringStoreResourceSet(
    name: String,
    vararg resources: FileResource
) : ResourceFileSet<FileResource>(
    name,
    ResourceTypeQualifier.Strings,
    resources.toList()
) {

    var avsReader = AvsReader.EMPTY
    var values = emptyArray<String?>()

    fun get(index: Int): String =
        if (index < 0 || index >= values.size) {
            error(
                """
                    Index $index out of bounds (0..${values.size - 1}). 
                    Have you called load() during application startup?
                    First path in store: ${files.firstOrNull()?.path}
                """.trimIndent()
            )
        } else {
            values[index] ?: avsReader[index].decodeToString().also { values[index] = it }
        }

    suspend fun load(
        environment: ResourceEnvironment = defaultResourceEnvironment,
        resourceReader: ResourceReader = defaultResourceReader
    ) {
        avsReader = AvsReader(readAll(environment, resourceReader))
        values = arrayOfNulls<String>(avsReader.size)
    }

}