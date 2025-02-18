package `fun`.adaptive.code.kotlin.writer

import `fun`.adaptive.code.kotlin.writer.model.KwFile
import `fun`.adaptive.code.kotlin.writer.model.KwModule
import `fun`.adaptive.code.kotlin.writer.model.KwStatementContainer
import `fun`.adaptive.code.kotlin.writer.model.KwSymbol
import kotlin.test.assertEquals

fun kwModuleTest(
    vararg expected: String,
    build: KwModule.() -> Unit
) {
    val writer =
        kotlinWriter {
            kwModule {
                build()
            }
        }

    writer.render()

    val files = writer.modules.first().files
    assertEquals(expected.size, files.size)
    files.forEachIndexed { index, file ->
        assertEquals(expected[index], file.renderedSource)
    }
}

fun kwFileTest(
    expected: String,
    build: KwFile.() -> Unit
) {
    val writer =
        kotlinWriter {
            kwModule {
                kwFile("Test") {
                    build()
                }
            }
        }

    writer.render()

    assertEquals(expected, writer.modules.first().files.first().renderedSource)
}

fun kwBodyTest(expected: String, builder: KwStatementContainer.() -> Unit) {
    kwFileTest(expected) {
        kwFunction(KwSymbol("testFun")) {
            kwBlockBody {
                builder()
            }
        }
    }
}