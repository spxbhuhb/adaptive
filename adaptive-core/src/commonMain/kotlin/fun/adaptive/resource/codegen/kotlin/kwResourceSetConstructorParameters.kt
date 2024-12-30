package `fun`.adaptive.resource.codegen.kotlin

import `fun`.adaptive.kotlin.writer.*
import `fun`.adaptive.kotlin.writer.model.KwCall
import `fun`.adaptive.kotlin.writer.model.KwExpressionScope
import `fun`.adaptive.kotlin.writer.model.KwSymbol
import `fun`.adaptive.resource.*

fun KwCall.resourceSetConstructorArguments(
    resourceFileSet: ResourceFileSet<*>,
    fileSymbol: KwSymbol
) {
    openFormat = true

    kwValueArgument("name") { kwConst(resourceFileSet.name) }

    resourceFileSet.files.forEach { file ->
        kwValueArgument {
            kwCall(fileSymbol) {
                kwValueArgument {
                    if (resourceFileSet.type.isUnstructured) {
                        kwConst(file.path)
                    } else {
                        kwConst(file.path.replaceAfterLast('.', "avs"))
                    }
                }
                resourceQualifiers(file.qualifiers)
            }
        }
    }
}

private fun KwCall.resourceQualifiers(qualifiers: Set<Qualifier>) =
    kwValueArgument {
        if (qualifiers.isEmpty()) {
            kwCall(KwKotlinSymbols.emptySet)
        } else {
            kwCall(KwKotlinSymbols.setOf) {
                qualifiers.forEach {
                    val qualifier = resourceQualifier(it)
                    if (qualifier != null) {
                        kwValueArgument { qualifier }
                    }
                }
            }
        }
    }

private fun KwExpressionScope.resourceQualifier(qualifier: Qualifier) =
    when (qualifier) {
        is LanguageQualifier ->
            kwCall(KwResourceSymbols.languageQualifier) {
                kwValueArgument { kwConst(qualifier.language) }
            }

        is RegionQualifier ->
            kwCall(KwResourceSymbols.regionQualifier) {
                kwValueArgument { kwConst(qualifier.region) }
            }

        is ThemeQualifier -> kwGetValue(qualifier.name) { kwGetObject(KwResourceSymbols.themeQualifier) }

        is DensityQualifier -> kwGetValue(qualifier.name) { kwGetObject(KwResourceSymbols.densityQualifier) }

        is ResourceTypeQualifier -> null

        else -> error("Unknown qualifier type: ${qualifier::class}")
    }