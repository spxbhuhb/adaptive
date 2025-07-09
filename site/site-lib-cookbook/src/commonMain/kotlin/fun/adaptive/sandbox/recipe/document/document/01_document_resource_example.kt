package `fun`.adaptive.sandbox.recipe.document.document

import `fun`.adaptive.cookbook.generated.resources.basic_example
import `fun`.adaptive.document.ui.basic.docDocument
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.document.Documents

/**
 * # From a Document resource
 *
 * Use the [docDocument](fragment://) fragment with a [document resource](def://) to render a
 * Markdown from application resources.
 *
 * See [Resource system](guide://) for more information.
 *
 * - the name of the resource must end with `.md`
 * - only Markdown is supported at the moment
 */
@Adaptive
fun documentResourceExample(): AdaptiveFragment {

    docDocument(Documents.basic_example)

    return fragment()
}