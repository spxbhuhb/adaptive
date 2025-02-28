package `fun`.adaptive.markdown.transform

import `fun`.adaptive.adat.encodeToPrettyJson
import `fun`.adaptive.grove.groveRuntimeCommon
import `fun`.adaptive.markdown.parse.parse
import `fun`.adaptive.markdown.parse.tokenize
import kotlin.test.BeforeTest
import kotlin.test.Test

class TransformTest {

    @BeforeTest
    fun setup() {
        groveRuntimeCommon()
    }

    @Test
    fun basic() {
        val source = "# Header"
        MarkdownToLfmTransform(parse(tokenize(source))).transform()
        // do I want to check anything here?
    }

}