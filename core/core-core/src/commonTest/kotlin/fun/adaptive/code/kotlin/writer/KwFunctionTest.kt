package `fun`.adaptive.code.kotlin.writer

import `fun`.adaptive.code.kotlin.writer.model.KwSymbol
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