package `fun`.adaptive.resource.graphics

import `fun`.adaptive.resource.ResourceFileSet
import `fun`.adaptive.resource.ResourceFileType

class GraphicsResourceSet(
    name: String,
    vararg resources: GraphicsResource
) : ResourceFileSet<GraphicsResource>(
    name,
    ResourceFileType.Graphics,
    resources.toList()
)