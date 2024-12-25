package `fun`.adaptive.kotlin.writer

import `fun`.adaptive.kotlin.writer.model.KwVisibility
import kotlin.test.Test

class FileTest {

    object Symbols {
        val files = kwSymbol("`fun`.adaptive.resource.file.Files")
        val fileResource = kwSymbol("`fun`.adaptive.resource.file.FileResource")
        val fileResourceSet = kwSymbol("`fun`.adaptive.resource.file.FileResourceSet")
        val emptySet = kwSymbol("emptySet")
        val lazy = kwSymbol("lazy")
    }
    
    @Test
    fun basic() {
        val writer = kotlinWriter { resourcesGenerated() }
        writer.render()
        writer.modules.forEach { module ->
            module.files.forEach { file -> println(file.renderedSource)}
        }
    }

    fun KotlinWriter.resourcesGenerated() {
        kwModule {
            kwFile("FileTest", "`fun`.adaptive.resource") {

                kwImport(Symbols.files)
                kwImport(Symbols.fileResource)
                kwImport(Symbols.fileResourceSet)

                kwProperty("testFile") {
                    isVal = true
                    receiver = Symbols.files
                    type = Symbols.fileResourceSet
                    getter = kwGetter {
                        kwGetValue("testFile") { kwGetObject("CommonMainFiles0") }
                    }
                }

                val init_testFile = kwSymbol("init_testFile")

                kwObject("CommonMainFiles0") {
                    kwProperty("testFile") {
                        isVal = true
                        type = Symbols.fileResourceSet
                        kwDelegation {
                            kwCall(Symbols.lazy) {
                                kwValueArgument {
                                    kwLambda {
                                        kwCall(init_testFile)
                                    }
                                }
                            }
                        }
                    }
                }

                kwFunction(init_testFile) {
                    visibility = KwVisibility.PRIVATE
                    body = kwExpressionBody {
                        kwCall(Symbols.fileResourceSet) {
                            kwValueArgument("name") { kwConst("testFile") }
                            kwValueArgument {
                                kwCall(Symbols.fileResource) {
                                    kwValueArgument { kwConst("adaptiveResources/test.txt") }
                                    kwValueArgument { kwCall(Symbols.emptySet) }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}