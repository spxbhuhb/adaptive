package hu.simplexion.z2.kotlin.util

import org.jetbrains.kotlin.GeneratedDeclarationKey
import org.jetbrains.kotlin.fir.declarations.FirDeclarationOrigin
import org.jetbrains.kotlin.fir.extensions.MemberGenerationContext
import org.jetbrains.kotlin.fir.symbols.SymbolInternals
import org.jetbrains.kotlin.fir.symbols.impl.FirClassSymbol
import org.jetbrains.kotlin.text

@OptIn(SymbolInternals::class)
fun FirClassSymbol<*>.superTypeContains(vararg names : String) : Boolean =
    this
        .fir
        .superTypeRefs
        .mapNotNull { it.source.text }
        .any { source ->
            names.any { it in source }
        }

fun MemberGenerationContext?.isFromPlugin(key : GeneratedDeclarationKey): Boolean {
    val owner = this?.owner ?: return false
    val ownerKey = (owner.origin as? FirDeclarationOrigin.Plugin)?.key ?: return false
    return ownerKey == key
}
