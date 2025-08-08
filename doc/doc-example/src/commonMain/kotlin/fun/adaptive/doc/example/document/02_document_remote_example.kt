package `fun`.adaptive.doc.example.document

import `fun`.adaptive.document.ui.basic.docDocument
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.document.DocumentResourceSet.Companion.remoteDocumentResource

/**
 * # From a remote resource
 *
 * Use the [remoteDocumentResource](function://DocumentResourceSet) function to fetch a document resource
 * from an URL.
 *
 * - the name of the resource must end with `.md`
 */
@Adaptive
fun documentRemoteExample(): AdaptiveFragment {

    val url = "https://raw.githubusercontent.com/spxbhuhb/adaptive/refs/heads/main/doc/doc-example/src/commonMain/adaptiveResources/documents/basic_example.md"

    docDocument(remoteDocumentResource(url))

    return fragment()
}