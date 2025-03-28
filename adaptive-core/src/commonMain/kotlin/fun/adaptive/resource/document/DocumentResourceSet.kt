package `fun`.adaptive.resource.document

import `fun`.adaptive.resource.ResourceFileSet
import `fun`.adaptive.resource.ResourceTypeQualifier

class DocumentResourceSet(
    name: String,
    vararg resources: DocumentResource
) : ResourceFileSet<DocumentResource>(
    name,
    ResourceTypeQualifier.Document,
    resources.toList()
) {
    companion object {

        fun remoteDocument(url : String) =
            DocumentResourceSet(
                REMOTE,
                DocumentResource(url, emptySet())
            )

        fun inlineDocument(name : String? = "<anonymous>", data : ByteArray) =
            DocumentResourceSet(
                INLINE + name
            ).also {
                it.cachedContent = data
            }

        fun inlineDocument(name : String? = "<anonymous>", content : String) =
            DocumentResourceSet(
                INLINE + name
            ).also {
                it.cachedContent = content.encodeToByteArray()
            }

    }
}