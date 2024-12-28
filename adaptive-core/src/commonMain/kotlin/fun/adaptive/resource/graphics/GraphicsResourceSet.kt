package `fun`.adaptive.resource.graphics

import `fun`.adaptive.resource.ResourceFileSet
import `fun`.adaptive.resource.ResourceTypeQualifier

class GraphicsResourceSet(
    name: String,
    vararg resources: GraphicsResource
) : ResourceFileSet<GraphicsResource>(
    name,
    ResourceTypeQualifier.Graphics,
    resources.toList()
)