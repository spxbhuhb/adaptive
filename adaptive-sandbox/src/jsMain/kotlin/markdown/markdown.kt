/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package markdown

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.markdown.fragment.document

@Adaptive
fun markdown() {
    document(source)
}

val source = """
# Hello Markdown World!

This is some text after the title.

## And a second level title

### And a third level title

#### I rarely use the fourth level, but still

This is great! Some **bold** text and some `code`.
And of course the crazy `` ` `` code constructs as well.
""".trimIndent()