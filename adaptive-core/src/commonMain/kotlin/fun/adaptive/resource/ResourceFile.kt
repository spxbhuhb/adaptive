package `fun`.adaptive.resource

open class ResourceFile(
    val path: String,
    val qualifiers: Set<Qualifier>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
    
        other as ResourceFile
        return path == other.path && qualifiers == other.qualifiers
    }
    
    override fun hashCode(): Int {
        var result = path.hashCode()
        result = 31 * result + qualifiers.hashCode()
        return result
    }
}