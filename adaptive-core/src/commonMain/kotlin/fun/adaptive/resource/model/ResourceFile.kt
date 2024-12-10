package `fun`.adaptive.resource.model

import `fun`.adaptive.resource.Qualifier

open class ResourceFile(
    val path: String,
    val qualifiers: Set<Qualifier>
)