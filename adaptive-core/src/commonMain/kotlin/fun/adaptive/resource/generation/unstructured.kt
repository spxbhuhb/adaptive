package `fun`.adaptive.resource.generation

import `fun`.adaptive.kotlin.writer.*
import `fun`.adaptive.kotlin.writer.model.KwExpressionScope
import `fun`.adaptive.kotlin.writer.model.KwFile
import `fun`.adaptive.kotlin.writer.model.KwVisibility
import `fun`.adaptive.resource.DensityQualifier
import `fun`.adaptive.resource.LanguageQualifier
import `fun`.adaptive.resource.Qualifier
import `fun`.adaptive.resource.RegionQualifier
import `fun`.adaptive.resource.ResourceFileSet
import `fun`.adaptive.resource.ThemeQualifier

fun KwFile.unstructured(sourceSet: String, index: Int, resources: List<ResourceFileSet<*>>) {

    if (resources.isEmpty()) return

    val resourceType = resources.first().type

    kwImport(resourceType.objectSymbol)
    kwImport(resourceType.fileSymbol)
    kwImport(resourceType.setSymbol)
    kwImport(KwResourceSymbols.languageQualifier)
    kwImport(KwResourceSymbols.regionQualifier)
    kwImport(KwResourceSymbols.themeQualifier)
    kwImport(KwResourceSymbols.densityQualifier)

    val resourceObjectName = resourceType.objectSymbol.name.substringAfterLast('.')
    val chunkObjectName = "$sourceSet$resourceObjectName$index"

    kwObject(chunkObjectName) {
        resources.forEach { resource ->
            kwProperty(resource.name) {
                isVal = true
                type = resourceType.setSymbol
                kwDelegation {
                    kwCall(KwKotlinSymbols.lazy) {
                        kwValueArgument {
                            kwLambda {
                                kwCall(kwSymbol("init_${resource.name}"))
                            }
                        }
                    }
                }
            }
        }
    }

    resources.forEach { resource ->

        kwProperty(resource.name) {
            isVal = true
            receiver = resourceType.objectSymbol
            type = resourceType.setSymbol
            getter = kwGetter {
                kwGetValue(resource.name) { kwGetObject(chunkObjectName) }
            }
        }

        kwFunction(kwSymbol("init_${resource.name}")) {
            visibility = KwVisibility.PRIVATE
            body = kwExpressionBody {
                kwCall(resourceType.setSymbol) {
                    kwValueArgument("name") { kwConst(resource.name) }

                    resource.files.forEach { file ->
                        kwValueArgument {
                            kwCall(resourceType.fileSymbol) {
                                kwValueArgument { kwConst(file.path) }
                                kwValueArgument {
                                    if (file.qualifiers.isEmpty()) {
                                        kwCall(KwKotlinSymbols.emptySet)
                                    } else {
                                        kwCall(KwKotlinSymbols.setOf) {
                                            file.qualifiers.forEach {
                                                kwValueArgument {
                                                    qualifier(it)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun KwExpressionScope.qualifier(qualifier: Qualifier) =
    when (qualifier) {
        is LanguageQualifier -> kwCall(KwResourceSymbols.languageQualifier) {
            kwValueArgument { kwConst(qualifier.language) }
        }

        is RegionQualifier -> kwCall(KwResourceSymbols.regionQualifier) {
            kwValueArgument { kwConst(qualifier.region) }
        }

        is ThemeQualifier -> kwGetValue(qualifier.name) { kwGetObject(KwResourceSymbols.themeQualifier) }

        is DensityQualifier -> kwGetValue(qualifier.name) { kwGetObject(KwResourceSymbols.densityQualifier) }

        else -> error("Unknown qualifier type: ${qualifier::class}")
    }