package `fun`.adaptive.resource.model

class GraphicsResourceSet(
    name: String,
    vararg resources: GraphicsResource
) : ResourceFileSet<GraphicsResource>(
    name,
    ResourceFileType.Graphics,
    resources.toList()
)