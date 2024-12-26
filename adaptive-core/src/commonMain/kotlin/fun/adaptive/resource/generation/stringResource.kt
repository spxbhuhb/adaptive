package `fun`.adaptive.resource.generation

import `fun`.adaptive.kotlin.writer.*
import `fun`.adaptive.kotlin.writer.model.KwFile
import `fun`.adaptive.resource.string.StringStoreResourceSet

fun KwFile.stringResource(
    storePropertyName: String,
    resourceSet: StringStoreResourceSet,
    avsIndex: Int,
    valueNames: List<String>
) {

    if (valueNames.isEmpty()) return

    kwImport(KwResourceSymbols.strings)
    kwImport(KwResourceSymbols.stringStoreResourceSet)
    kwImport(KwResourceSymbols.fileResource)
    kwImport(KwResourceSymbols.languageQualifier)
    kwImport(KwResourceSymbols.regionQualifier)
    kwImport(KwResourceSymbols.themeQualifier)
    kwImport(KwResourceSymbols.densityQualifier)

    if (avsIndex == 0) {
        storeProperty(storePropertyName, resourceSet)
    }

    valueNames.forEachIndexed { index, valueName ->

        kwProperty(valueName) {
            isVal = true
            receiver = KwResourceSymbols.strings
            getter = kwGetter {
                kwCall(KwKotlinSymbols.get) {
                    dispatchReceiver = kwGetValue(storePropertyName)
                    kwValueArgument { kwConst(index) }
                }
            }
        }

    }
}

private fun KwFile.storeProperty(
    storePropertyName: String,
    resourceSet: StringStoreResourceSet
) {
    kwProperty(storePropertyName) {
        kwInitializer {
            kwCall(KwResourceSymbols.stringStoreResourceSet) {
                resourceSetConstructorArguments(resourceSet, KwResourceSymbols.fileResource)
            }
        }
    }
}
