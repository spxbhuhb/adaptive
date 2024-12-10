package `fun`.adaptive.resource.model

import `fun`.adaptive.resource.Qualifier

class GraphicsResource(
    path: String,
    qualifiers: Set<Qualifier>
) : ResourceFile(
    path,
    qualifiers
)