package hu.simplexion.adaptive.kotlin.adat.ir.immutable

import hu.simplexion.adaptive.kotlin.adat.Names
import hu.simplexion.adaptive.kotlin.adat.ir.AdatIrBuilder
import hu.simplexion.adaptive.kotlin.common.asClassId
import hu.simplexion.adaptive.kotlin.common.isSubclassOf
import hu.simplexion.adaptive.wireformat.signature.WireFormatType
import hu.simplexion.adaptive.wireformat.signature.parseTypeSignature
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.types.classFqName
import org.jetbrains.kotlin.ir.util.isEnumClass
import org.jetbrains.kotlin.ir.util.isFunction
import org.jetbrains.kotlin.ir.util.isKFunction
import org.jetbrains.kotlin.ir.util.isKSuspendFunction

fun AdatIrBuilder.isImmutable(signature: String): Boolean =
    isImmutable(parseTypeSignature(signature)) // FIXME cache the signatures as well so we don't have to parse them again and again

fun AdatIrBuilder.isImmutable(type: WireFormatType): Boolean {

    val name = type.name

    if (type.short) {
        return checkNotNull(pluginContext.shortImmutableCache[name]) { "missing short immutable cache entry for $name" }
    }

    val cache = pluginContext.qualifiedImmutableCache

    val cached = cache[name]
    if (cached != null) return cached

    return if (name.startsWith("kotlin.collections.")) {
        collections(name, type.generics)
    } else {
        instance(name, type.generics)
    }
}


private fun AdatIrBuilder.collections(name: String, generics: List<WireFormatType>): Boolean {
    return when (name) {
        "kotlin.collections.List" -> generics.none { isImmutable(it) }
        "kotlin.collections.Map" -> generics.none { isImmutable(it) }
        "kotlin.collections.Set" -> generics.none { isImmutable(it) }
        else -> throw UnsupportedOperationException("collection type $name is not supported")
    }
}

private fun AdatIrBuilder.instance(name: String, generics: List<WireFormatType>): Boolean {

    if (generics.isNotEmpty()) {
        pluginContext.qualifiedImmutableCache[name] = false
        return false
    }

    val classSymbol = irContext.referenceClass(name.asClassId)
    checkNotNull(classSymbol) { "missing class: $name" }

    val irClass = classSymbol.owner
    // TODO open class
    val immutable = when {

        // FIXME are functions immutable?
        classSymbol.isFunction() -> true
        classSymbol.isKFunction() -> true
        classSymbol.isKSuspendFunction() -> true

        // FIXME are enum classes immutable?
        irClass.isEnumClass -> true

        irClass.isSubclassOf(pluginContext.adatClass) -> properties(irClass)

        else -> false
    }

    pluginContext.qualifiedImmutableCache[name] = immutable
    return immutable
}

fun AdatIrBuilder.properties(irClass: IrClass): Boolean {
    val cache = pluginContext.qualifiedImmutableCache

    for (declaration in irClass.declarations) {
        if (declaration !is IrProperty) continue

        val name = declaration.name
        if (name == Names.ADAT_CONTEXT || name == Names.ADAT_COMPANION) continue

        if (declaration.isVar) return false

        val backingField = declaration.backingField ?: continue

        val type = backingField.type
        val typeFqName = type.classFqName?.asString() ?: return false
        val signature = "L$typeFqName;"

        val cached = cache[signature]
        if (cached == false) return false
        if (cached == true) continue

        val isImmutable = isImmutable(signature)

        cache[signature] = isImmutable
        if (! isImmutable) return false
    }

    return true
}