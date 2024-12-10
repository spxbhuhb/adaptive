package `fun`.adaptive.resource.model

import `fun`.adaptive.resource.Qualifier

class FontResource(
    path: String,
    qualifiers: Set<Qualifier>
) : ResourceFile(
    path,
    qualifiers
)