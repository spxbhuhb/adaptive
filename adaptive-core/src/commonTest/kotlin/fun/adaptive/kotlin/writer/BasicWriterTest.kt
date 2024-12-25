package `fun`.adaptive.kotlin.writer

import `fun`.adaptive.kotlin.writer.model.KwSymbol
import `fun`.adaptive.kotlin.writer.model.KwVisibility
import `fun`.adaptive.resource.generation.KwResourceSymbols
import kotlin.test.Test
import kotlin.test.assertEquals

class BasicWriterTest {

    @Test
    fun basicFile() {
        val writer = kotlinWriter { resourcesGeneratedFile() }
        writer.render()
        assertEquals(fileSource, writer.modules.first().files.first().renderedSource)
    }

    val fileSource = """
        package `fun`.adaptive.resource
        
        import `fun`.adaptive.resource.file.Files
        import `fun`.adaptive.resource.file.FileResource
        import `fun`.adaptive.resource.file.FileResourceSet
        
        val Files.testFile : FileResourceSet
            get() = CommonMainFiles0.testFile
        
        private object CommonMainFiles0 {
        
            val testFile : FileResourceSet by lazy { init_testFile() }
        
        }
        
        private fun init_testFile() =
            FileResourceSet(name = "testFile", FileResource("adaptiveResources/test.txt", emptySet()))
        
        
        """.trimIndent()

    fun KotlinWriter.resourcesGeneratedFile() {
        kwModule {
            kwFile("FileTest", "`fun`.adaptive.resource") {

                kwImport(KwResourceSymbols.files)
                kwImport(KwResourceSymbols.fileResource)
                kwImport(KwResourceSymbols.fileResourceSet)

                kwProperty("testFile") {
                    isVal = true
                    receiver = KwResourceSymbols.files
                    type = KwResourceSymbols.fileResourceSet
                    getter = kwGetter {
                        kwGetValue("testFile") { kwGetObject("CommonMainFiles0") }
                    }
                }

                val init_testFile = kwSymbol("init_testFile")

                kwObject("CommonMainFiles0") {
                    kwProperty("testFile") {
                        isVal = true
                        type = KwResourceSymbols.fileResourceSet
                        kwDelegation {
                            kwCall(KwKotlinSymbols.lazy) {
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
                        kwCall(KwResourceSymbols.fileResourceSet) {
                            kwValueArgument("name") { kwConst("testFile") }
                            kwValueArgument {
                                kwCall(KwResourceSymbols.fileResource) {
                                    kwValueArgument { kwConst("adaptiveResources/test.txt") }
                                    kwValueArgument { kwCall(KwKotlinSymbols.emptySet) }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    @Test
    fun basicString() {
        val writer = kotlinWriter { resourcesGeneratedString() }
        writer.render()
        assertEquals(stringSource, writer.modules.first().files.first().renderedSource)
    }

    val stringSource = """
        package `fun`.adaptive.resource
        
        import `fun`.adaptive.resource.string.Strings
        import `fun`.adaptive.resource.string.StringStoreResourceSet
        import `fun`.adaptive.resource.file.FileResource
        
        val commonStrings =
            StringStoreResourceSet(name = "common", FileResource("strings/common-cs-CZ.avs", emptySet()), FileResource("strings/common-hu-HU.avs", emptySet()))
        
        val Strings.v1
            get() = commonStrings.get(0)
        
        val Strings.v2
            get() = commonStrings.get(1)
        
        
        """.trimIndent()

    fun KotlinWriter.resourcesGeneratedString() {
        kwModule {
            kwFile("StringTest", "`fun`.adaptive.resource") {

                kwImport(KwResourceSymbols.strings)
                kwImport(KwResourceSymbols.stringStoreResourceSet)
                kwImport(KwResourceSymbols.fileResource)

                kwProperty("commonStrings") {
                    isVal = true
                    initializer = kwInitializer {
                        kwCall(KwResourceSymbols.stringStoreResourceSet) {
                            kwValueArgument("name") { kwConst("common") }
                            kwValueArgument {
                                kwCall(KwResourceSymbols.fileResource) {
                                    kwValueArgument { kwConst("strings/common-cs-CZ.avs") }
                                    kwValueArgument { kwCall(KwKotlinSymbols.emptySet) }
                                }
                            }
                            kwValueArgument {
                                kwCall(KwResourceSymbols.fileResource) {
                                    kwValueArgument { kwConst("strings/common-hu-HU.avs") }
                                    kwValueArgument { kwCall(KwKotlinSymbols.emptySet) }
                                }
                            }
                        }
                    }
                }

                listOf("v1", "v2").forEachIndexed { index, name ->
                    kwProperty(name) {
                        isVal = true
                        receiver = KwResourceSymbols.strings
                        getter = kwGetter {
                            kwCall(KwSymbol("get")) {
                                dispatchReceiver = kwGetValue("commonStrings")
                                kwValueArgument { kwConst(index) }
                            }
                        }
                    }
                }
            }
        }
    }
}