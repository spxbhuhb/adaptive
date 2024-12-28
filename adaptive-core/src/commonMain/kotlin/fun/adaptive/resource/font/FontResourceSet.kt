package `fun`.adaptive.resource.font

import `fun`.adaptive.resource.ResourceFileSet
import `fun`.adaptive.resource.ResourceTypeQualifier

class FontResourceSet(
    name: String,
    vararg resources: FontResource
) : ResourceFileSet<FontResource>(
    name,
    ResourceTypeQualifier.Font,
    resources.toList()
)