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
        val richText = MarkdownToLfmTransform(parse(tokenize(source))).transform()
        // do I want to check anything here?
        println(richText.model.encodeToPrettyJson())
    }

    val source = """    
        Link in [Adaptive](https://adaptive.fun) text.
    """.trimIndent()
}