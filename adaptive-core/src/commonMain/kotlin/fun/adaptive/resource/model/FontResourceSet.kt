package `fun`.adaptive.resource.model

class FontResourceSet(
    name: String,
    vararg resources: FontResource
) : ResourceFileSet<FontResource>(
    name,
    ResourceFileType.Font,
    resources.toList()
)