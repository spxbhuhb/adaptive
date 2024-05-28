/*
 * This code has been copied from Compose Multiplatform: https://github.com/JetBrains/compose-multiplatform
 */

package hu.simplexion.adaptive.resource

/**
 * Represents a resource with an ID and a set of resource items.
 *
 * @property id The ID of the resource.
 * @property items The set of resource items associated with the resource.
 */
open class Resource (
    val id: String,
    val items: Set<ResourceItem>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Resource

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    val uri : String
        get() = DefaultResourceReader.getUri(items.single().path)

}

data class ResourceItem(
    val qualifiers: Set<Qualifier>,
    val path: String,
    val offset: Long,
    val size: Long,
)