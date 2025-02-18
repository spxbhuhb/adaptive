package `fun`.adaptive.resource.codegen.kotlin

import `fun`.adaptive.code.kotlin.writer.*
import `fun`.adaptive.code.kotlin.writer.model.KwFile
import `fun`.adaptive.code.kotlin.writer.model.KwVisibility
import `fun`.adaptive.resource.ResourceFileSet

fun KwFile.unstructuredResource(accessorObjectName : String, resources: List<ResourceFileSet<*>>) {

    if (resources.isEmpty()) return

    val resourceType = resources.first().type

    kwImport(resourceType.objectSymbol)
    kwImport(resourceType.fileSymbol)
    kwImport(resourceType.setSymbol)
    kwImport(KwResourceSymbols.languageQualifier)
    kwImport(KwResourceSymbols.regionQualifier)
    kwImport(KwResourceSymbols.themeQualifier)
    kwImport(KwResourceSymbols.densityQualifier)

    kwObject(accessorObjectName) {
        resources.forEach { resource ->
            kwProperty(resource.name) {
                isVal = true
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
            getter = kwGetter {
                kwGetValue(resource.name) { kwGetObject(accessorObjectName) }
            }
        }

        kwFunction(kwSymbol("init_${resource.name}")) {
            visibility = KwVisibility.PRIVATE
            kwExpressionBody {
                kwCall(resourceType.setSymbol) {
                    resourceSetConstructorArguments(resource, packageName, resourceType.fileSymbol)
                }
            }
        }
    }
}
