package `fun`.adaptive.resource

open class ResourceFileSet<T : ResourceFile>(
    val name: String,
    val type: ResourceTypeQualifier,
    val files: List<T>
) {

    companion object {
        const val REMOTE = "remote:"
        const val INLINE = "inline:"
    }

    var lastEnvironment: ResourceEnvironment? = null
    var lastResult: T? = null

    open val cacheResource
        get() = false

    var cachedContent: ByteArray? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ResourceFileSet<*>) return false
    
        if (name != other.name) return false
        if (type != other.type) return false
        if (files != other.files) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + files.hashCode()
        return result
    }

    override fun toString(): String {
        return "ResourceFileSet(name='$name', type=$type, resources=${files.joinToString()})"
    }

    val isInline : Boolean
        get() = name.startsWith(INLINE)

    val uri : String
        get() = getUri(defaultResourceEnvironment, defaultResourceReader)

    fun getUri(
        environment: ResourceEnvironment,
        resourceReader: ResourceReader
    ) : String {
        if (isInline) return name.removePrefix(INLINE)

        val file = getByEnvironment(environment)
        return resourceReader.getUri(file.path)
    }

    suspend fun readAll(
        environment: ResourceEnvironment = defaultResourceEnvironment,
        resourceReader: ResourceReader = defaultResourceReader
    ): ByteArray {

        if (isInline) return cachedContent ?: ByteArray(0)

        // When the environment is the same, no need to run filtering again
        // this will probably cover 99% of the cases.

        val bytes: ByteArray

        if (environment == lastEnvironment) {
            val cached = cachedContent
            if (cacheResource && cached != null) {
                bytes = cached
            } else {
                bytes = resourceReader.read(lastResult !!.path).cache()
            }
        } else {
            val file = getByEnvironment(environment)
            bytes = resourceReader.read(file.path).cache()
            lastEnvironment = environment
        }

        return bytes
    }

    fun ByteArray.cache(): ByteArray {
        if (cacheResource) {
            cachedContent = this
        }
        return this
    }

    fun getByEnvironment(environment: ResourceEnvironment): T {
        //Priority of environments: https://developer.android.com/guide/topics/resources/providing-resources#table2
        files
            .filterByLocale(environment.language, environment.region)
            .also { if (it.size == 1) return it.first() }
            .filterBy(environment.theme)
            .also { if (it.size == 1) return it.first() }
            .filterBy(environment.density)
            .also { if (it.size == 1) return it.first() }
            .let { items ->
                if (items.isEmpty()) {
                    error("Resource with name='$name' not found for environment $environment")
                } else {
                    error("Resource with name='$name' has more than one file: ${items.joinToString { it.path }}")
                }
            }
    }

    private fun List<T>.filterBy(qualifier: Qualifier): List<T> {
        //Android has a slightly different algorithm,
        //but it provides the same result: https://developer.android.com/guide/topics/resources/providing-resources#BestMatch

        //filter items with the requested qualifier
        val withQualifier = filter { item ->
            item.qualifiers.any { it == qualifier }
        }

        if (withQualifier.isNotEmpty()) return withQualifier

        //items with no requested qualifier type (default)
        return filter { item ->
            item.qualifiers.none { it::class == qualifier::class }
        }
    }

    // we need to filter by language and region together because there is slightly different logic:
    // 1) if there is the exact match language+region then use it
    // 2) if there is the language WITHOUT region match then use it
    // 3) in other cases use items WITHOUT language and region qualifiers at all
    // issue: https://github.com/JetBrains/compose-multiplatform/issues/4571
    private fun List<T>.filterByLocale(language: LanguageQualifier, region: RegionQualifier): List<T> {

        val withLanguage = filter { item ->
            item.qualifiers.any { it == language }
        }

        val withExactLocale = withLanguage.filter { item ->
            item.qualifiers.any { it == region }
        }

        //if there are the exact language + the region items
        if (withExactLocale.isNotEmpty()) return withExactLocale

        val withDefaultRegion = withLanguage.filter { item ->
            item.qualifiers.none { it is RegionQualifier }
        }

        //if there are the language without a region items
        if (withDefaultRegion.isNotEmpty()) return withDefaultRegion

        //items without any locale qualifiers
        return filter { item ->
            item.qualifiers.none { it is LanguageQualifier || it is RegionQualifier }
        }
    }
}