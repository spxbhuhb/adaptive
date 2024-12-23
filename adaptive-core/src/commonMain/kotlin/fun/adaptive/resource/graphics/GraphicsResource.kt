package `fun`.adaptive.resource.graphics

import `fun`.adaptive.resource.Qualifier
import `fun`.adaptive.resource.ResourceFile

class GraphicsResource(
    path: String,
    qualifiers: Set<Qualifier>
) : ResourceFile(
    path,
    qualifiers
)