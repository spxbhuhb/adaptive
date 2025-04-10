package `fun`.adaptive.resource.font

import `fun`.adaptive.resource.Qualifier
import `fun`.adaptive.resource.ResourceFile

class FontResource(
    path: String,
    qualifiers: Set<Qualifier>
) : ResourceFile(
    path,
    qualifiers
)