package `fun`.adaptive.resource.font

import `fun`.adaptive.resource.ResourceFileSet
import `fun`.adaptive.resource.ResourceFileType

class FontResourceSet(
    name: String,
    vararg resources: FontResource
) : ResourceFileSet<FontResource>(
    name,
    ResourceFileType.Font,
    resources.toList()
)