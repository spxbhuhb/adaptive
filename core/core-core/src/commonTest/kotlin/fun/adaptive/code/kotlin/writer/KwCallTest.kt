package `fun`.adaptive.code.kotlin.writer

import `fun`.adaptive.code.kotlin.writer.model.KwSymbol
import kotlin.test.Test

class KwCallTest {

    @Test
    fun noArg() {
        val expected =
            """
                fun testFun() =
                    testFun2()
            """.trimIndent()

        kwBodyTest(expected) {
            kwCall(KwSymbol("testFun2"))
        }
    }

    @Test
    fun openNoArg() {
        val expected =
            """
                fun testFun() =
                    testFun2()
            """.trimIndent()

        kwBodyTest(expected) {
            kwCall(KwSymbol("testFun2")) { openFormat = true }
        }
    }

    @Test
    fun openOneArg() {
        val expected =
            """
                fun testFun() =
                    testFun2(
                        12
                    )
            """.trimIndent()

        kwBodyTest(expected) {
            kwCall(KwSymbol("testFun2")) {
                openFormat = true
                kwValueArgument { kwConst(12) }
            }
        }
    }

    @Test
    fun closedOneArg() {
        val expected =
            """
                fun testFun() =
                    testFun2(12)
            """.trimIndent()

        kwBodyTest(expected) {
            kwCall(KwSymbol("testFun2")) {
                kwValueArgument { kwConst(12) }
            }
        }
    }

    @Test
    fun openMultiArgs() {
        val expected =
            """
                fun testFun() =
                    testFun2(
                        12,
                        23
                    )
            """.trimIndent()

        kwBodyTest(expected) {
            kwCall(KwSymbol("testFun2")) {
                openFormat = true
                kwValueArgument { kwConst(12) }
                kwValueArgument { kwConst(23) }
            }
        }
    }

    @Test
    fun closedMultiArgs() {
        val expected =
            """
                fun testFun() =
                    testFun2(12, 23)
            """.trimIndent()

        kwBodyTest(expected) {
            kwCall(KwSymbol("testFun2")) {
                kwValueArgument { kwConst(12) }
                kwValueArgument { kwConst(23) }
            }
        }
    }

    @Test
    fun openNested() {
        val expected =
            """
                fun testFun() =
                    testFun2(
                        12,
                        testFun3(
                            23
                        )
                    )
            """.trimIndent()

        kwBodyTest(expected) {
            kwCall(KwSymbol("testFun2")) {
                openFormat = true
                kwValueArgument { kwConst(12) }
                kwValueArgument {
                    kwCall(KwSymbol("testFun3")) {
                        openFormat = true
                        kwValueArgument { kwConst(23) }
                    }
                }
            }
        }
    }

}