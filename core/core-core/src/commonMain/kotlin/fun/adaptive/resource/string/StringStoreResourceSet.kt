package `fun`.adaptive.resource.string

import `fun`.adaptive.foundation.unsupported
import `fun`.adaptive.resource.*
import `fun`.adaptive.resource.avs.AvsReader
import `fun`.adaptive.resource.file.FileResource

class StringStoreResourceSet(
    name: String,
    vararg resources: FileResource
) : ResourceFileSet<FileResource>(
    name,
    ResourceTypeQualifier.Strings,
    resources.toList()
) {

    var values = mutableMapOf<ResourceKey,String>()

    @Deprecated("use get(key) instead")
    fun get(index: Int): String = unsupported()

    fun get(key : ResourceKey)  =
        values[key] ?:
            error(
                """
                    Key $key is not in this string store. 
                    Have you called load() during application startup?
                    First path in store: ${files.firstOrNull()?.path}
                """.trimIndent()
            )

    fun getOrNull(key: ResourceKey) =
        values[key]

    suspend fun load(
        environment: ResourceEnvironment = defaultResourceEnvironment,
        resourceReader: ResourceReader = defaultResourceReader
    ) {
       for (item in AvsReader(readAll(environment, resourceReader))) {
           val keySize = item[0].toInt()
           val key = item.decodeToString(1, keySize + 1)

           val valueOffset = keySize + 1
           val value = item.decodeToString(valueOffset, item.size)

           values[key] = value
       }
    }


}