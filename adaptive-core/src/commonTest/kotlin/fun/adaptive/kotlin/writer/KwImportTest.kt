package `fun`.adaptive.kotlin.writer

import `fun`.adaptive.kotlin.writer.model.KwSymbol
import kotlin.test.Test

class KwImportTest {

    @Test
    fun used() {
        val expected =
            """
                import `fun`.adaptive.kotlin.writer.test.testSymbol
                
                val testProp =
                    testSymbol()
            """.trimIndent()

        val symbol = KwSymbol("`fun`.adaptive.kotlin.writer.test.testSymbol")

        kwFileTest(expected) {
            kwImport(symbol)
            kwProperty("testProp") {
                kwInitializer { kwCall(symbol) }
            }
        }
    }

    @Test
    fun unused() {
        val expected =
            """
                val testProp =
                    1
            """.trimIndent()

        val symbol = KwSymbol("`fun`.adaptive.kotlin.writer.test.testSymbol")

        kwFileTest(expected) {
            kwImport(symbol)
            kwProperty("testProp") {
                kwInitializer { kwConst(1) }
            }
        }
    }

    @Test
    fun multiFile() {

        val expected1 =
            """
                import `fun`.adaptive.kotlin.writer.test.testSymbol
                
                val testProp =
                    testSymbol()
            """.trimIndent()

        val expected2 =
            """
                val testProp =
                    1
            """.trimIndent()

        val symbol = KwSymbol("`fun`.adaptive.kotlin.writer.test.testSymbol")

        kwModuleTest(expected1, expected2) {
            kwFile("Test1") {
                kwImport(symbol)
                kwProperty("testProp") {
                    kwInitializer { kwCall(symbol) }
                }
            }
            kwFile("Test2") {
                kwImport(symbol)
                kwProperty("testProp") {
                    kwInitializer { kwConst(1) }
                }
            }

        }
    }
}