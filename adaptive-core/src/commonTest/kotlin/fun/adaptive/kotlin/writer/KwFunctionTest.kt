package `fun`.adaptive.kotlin.writer

import `fun`.adaptive.kotlin.writer.model.KwSymbol
import kotlin.test.Test

class KwFunctionTest {

    @Test
    fun empty() {
        val expected =
            """
                fun testFun() {
                }
            """.trimIndent()

        kwFileTest(expected) {
            kwFunction(KwSymbol("testFun")) { kwBlockBody {  } }
        }
    }

}