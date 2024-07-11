package hu.simplexion.adaptive.kotlin.wireformat

import hu.simplexion.adaptive.kotlin.adat.ir.AdatIrBuilder
import hu.simplexion.adaptive.kotlin.common.AbstractIrBuilder
import hu.simplexion.adaptive.kotlin.common.AbstractPluginContext
import hu.simplexion.adaptive.kotlin.common.AdaptiveFqNames
import hu.simplexion.adaptive.kotlin.common.asClassId
import hu.simplexion.adaptive.wireformat.signature.WireFormatType
import hu.simplexion.adaptive.wireformat.signature.parseSignature
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.declarations.IrAttributeContainer
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrConstKind
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrConstImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.types.IrSimpleType
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.ir.visitors.IrElementVisitor

fun AdatIrBuilder.sensibleDefault(signature: String): IrExpression? {

    val cache = pluginContext.sensibleCache

    val cached = cache.getOrDefault(signature, MISSING)
    if (cached !== MISSING) return cached?.deepCopyWithSymbols()

    val type = parseSignature(signature)
    if (type.nullable) {
        cache[signature] = null
        return null
    }

    val generics = type.generics.map { pluginContext.toIrType(it) }

    val name = type.name
    val length = name.length

    val expression =

        if (length > 3) {
            when {
                name == "kotlin.Array" -> array(name, generics)
                name.startsWith("kotlin.collections.") -> collections(name, generics)
                name.startsWith("kotlinx.datetime.") || name.startsWith("kotlin.time") -> datetime(name)
                else -> instance(name, generics)
            }
        } else {
            when (length) {
                1 -> when (name) {
                    "T" -> irConst("")
                    "Z" -> irConst(false)
                    "I" -> irConst(0)
                    "U" -> instance(AdaptiveFqNames.UUID.asString(), generics)
                    "J" -> irConst(0L)
                    "D" -> IrConstImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, irBuiltIns.doubleType, IrConstKind.Double, 0.0)
                    "S" -> IrConstImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, irBuiltIns.shortType, IrConstKind.Short, 0)
                    "B" -> IrConstImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, irBuiltIns.byteType, IrConstKind.Byte, 0)
                    "F" -> IrConstImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, irBuiltIns.floatType, IrConstKind.Float, 0f)
                    "C" -> IrConstImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, irBuiltIns.charType, IrConstKind.Char, '\u0000')
                    "0" -> irNull() // FIXME unit is converted to null, how to handle function types in sensible
                    else -> instance(name, generics)
                }

                2 -> when (name) {

                    "[Z" -> newEmptyArray(irBuiltIns.booleanType)
                    "[I" -> newEmptyArray(irBuiltIns.intType)
                    "[S" -> newEmptyArray(irBuiltIns.shortType)
                    "[B" -> newEmptyArray(irBuiltIns.byteType)
                    "[J" -> newEmptyArray(irBuiltIns.longType)
                    "[F" -> newEmptyArray(irBuiltIns.floatType)
                    "[D" -> newEmptyArray(irBuiltIns.doubleType)
                    "[C" -> newEmptyArray(irBuiltIns.charType)

                    "+I" -> IrConstImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, pluginContext.uIntType, IrConstKind.Int, 0)
                    "+S" -> IrConstImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, pluginContext.uShortType, IrConstKind.Int, 0)
                    "+B" -> IrConstImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, pluginContext.uByteType, IrConstKind.Int, 0)
                    "+J" -> IrConstImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, pluginContext.uLongType, IrConstKind.Long, 0L)

                    else -> instance(name, generics)
                }

                else -> when (name) {
                    "[+I" -> TODO()
                    "[+S" -> TODO()
                    "[+B" -> TODO()
                    "[+J" -> TODO()
                    else -> instance(name, generics)
                }
            }
        }

    cache[signature] = expression
    return expression
}

private fun AbstractPluginContext.toIrType(wireFormatType: WireFormatType): IrSimpleType {
    // TODO should we cache wireFormatType - irType pairs, probably yes

    val generics = wireFormatType.generics.map { toIrType(it) }

    val name = wireFormatType.name
    val length = name.length
    val irBuiltIns = irContext.irBuiltIns

    fun symbol(fqName: String, generics: List<IrType>): IrSimpleType {
        val classSymbol = irContext.referenceClass(fqName.asClassId)

        checkNotNull(classSymbol) { "missing class: $fqName" }

        return if (generics.isEmpty()) {
            classSymbol.defaultType as IrSimpleType
        } else {
            classSymbol.typeWith(generics)
        }
    }

    if (length > 3) {
        // surely not a primitive wireFormatType
        return symbol(name, generics)
    }

    return when (length) {
        1 -> when (name) {
            "T" -> irBuiltIns.stringType
            "Z" -> irBuiltIns.booleanType
            "I" -> irBuiltIns.intType
            "U" -> adaptiveSymbols.uuid.typeWith(generics)

            "J" -> irBuiltIns.longType
            "D" -> irBuiltIns.doubleType
            "S" -> irBuiltIns.shortType
            "B" -> irBuiltIns.byteType
            "F" -> irBuiltIns.floatType
            "C" -> irBuiltIns.charType

            "*" -> irBuiltIns.anyType

            "0" -> irBuiltIns.unitType // FIXME unit type for sensible

            else -> symbol(name, generics)
        }

        2 -> when (name) {

            "[Z" -> irBuiltIns.booleanArray.defaultType
            "[I" -> irBuiltIns.intArray.defaultType
            "[S" -> irBuiltIns.shortArray.defaultType
            "[B" -> irBuiltIns.byteArray.defaultType
            "[J" -> irBuiltIns.longArray.defaultType
            "[F" -> irBuiltIns.floatArray.defaultType
            "[D" -> irBuiltIns.doubleArray.defaultType
            "[C" -> irBuiltIns.charArray.defaultType

            "+I" -> kotlinUnsignedSymbols.uInt.defaultType
            "+S" -> kotlinUnsignedSymbols.uShort.defaultType
            "+B" -> kotlinUnsignedSymbols.uByte.defaultType
            "+J" -> kotlinUnsignedSymbols.uLong.defaultType
            else -> symbol(name, generics)
        }

        else -> when (name) {
            "[+I" -> kotlinUnsignedSymbols.uIntArray.defaultType
            "[+S" -> kotlinUnsignedSymbols.uShortArray.defaultType
            "[+B" -> kotlinUnsignedSymbols.uByteArray.defaultType
            "[+J" -> kotlinUnsignedSymbols.uLongArray.defaultType
            else -> symbol(name, generics)
        }
    } as IrSimpleType
}

private fun AbstractIrBuilder.instance(name: String, generics: List<IrType>): IrConstructorCall? {
    val classSymbol = irContext.referenceClass(name.asClassId)

    checkNotNull(classSymbol) { "missing class: $name" }

    if (classSymbol.isFunction() || classSymbol.isKFunction() || classSymbol.isKSuspendFunction()) return null

    val constructor = classSymbol.constructors.firstOrNull { it.owner.valueParameters.isEmpty() }

    checkNotNull(constructor) { "class $name does not have an empty constructor, set a default manually" }

    val type = if (generics.isEmpty()) {
        classSymbol.defaultType as IrSimpleType
    } else {
        classSymbol.typeWith(generics)
    }

    return IrConstructorCallImpl(
        SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
        type,
        constructor,
        typeArgumentsCount = generics.size,
        constructorTypeArgumentsCount = 0,
        valueArgumentsCount = 0
    ).also {
        generics.forEachIndexed { index, irType ->
            it.putTypeArgument(index, irType)
        }
    }
}

private fun AbstractIrBuilder.newEmptyArray(typeArgument: IrType) =
    irCall(pluginContext.kotlinSymbols.emptyArray, irBuiltIns.arrayClass, listOf(typeArgument))

private fun AbstractIrBuilder.array(name: String, generics: List<IrType>): IrCall {
    val symbols = pluginContext.kotlinSymbols
    return irCall(symbols.emptyArray, symbols.array, generics)
}

private fun AbstractIrBuilder.collections(name: String, generics: List<IrType>): IrCall {
    val symbols = pluginContext.kotlinSymbols
    return when (name) {
        "kotlin.collections.List" -> irCall(symbols.emptyList, symbols.list, generics)
        "kotlin.collections.Map" -> irCall(symbols.emptyMap, symbols.map, generics)
        "kotlin.collections.Set" -> irCall(symbols.emptySet, symbols.set, generics)
        "kotlin.collections.MutableList" -> irCall(symbols.mutableListOf, symbols.mutableList, generics)
        "kotlin.collections.MutableMap" -> irCall(symbols.mutableMapOf, symbols.mutableMap, generics)
        "kotlin.collections.MutableSet" -> irCall(symbols.mutableSetOf, symbols.mutableSet, generics)
        else -> throw UnsupportedOperationException("collection type $name is not supported")
    }
}

private fun AbstractIrBuilder.datetime(name: String): IrCall {
    val symbols = pluginContext.adaptiveSymbols
    val types = pluginContext.dateTimeTypes

    return when (name) {
        "kotlinx.datetime.Instant" -> irCall(symbols.instant, types.instant, emptyList())
        "kotlinx.datetime.LocalDateTime" -> irCall(symbols.localDateTime, types.localDateTime, emptyList())
        "kotlinx.datetime.LocalDate" -> irCall(symbols.localDate, types.localDate, emptyList())
        "kotlinx.datetime.LocalTime" -> irCall(symbols.localTime, types.localTime, emptyList())
        "kotlin.time.Duration" -> irCall(symbols.zeroDuration, types.duration, emptyList())
        else -> throw UnsupportedOperationException("datetime type $name is not supported")
    }
}

private fun irCall(function: IrSimpleFunctionSymbol, type: IrClassSymbol, typeArguments: List<IrType>): IrCall =
    IrCallImpl(
        UNDEFINED_OFFSET,
        UNDEFINED_OFFSET,
        type.typeWith(typeArguments),
        function,
        typeArguments.size,
        0
    ).also {
        typeArguments.forEachIndexed { index, irType ->
            it.putTypeArgument(index, irType)
        }
    }

private object MISSING : IrExpression() {
    override var attributeOwnerId: IrAttributeContainer
        get() = throw UnsupportedOperationException()
        set(@Suppress("UNUSED_PARAMETER") v) = throw UnsupportedOperationException()
    override val endOffset = 0
    override var originalBeforeInline: IrAttributeContainer? = null
    override val startOffset = 0
    override var type: IrType
        get() = throw UnsupportedOperationException()
        set(@Suppress("UNUSED_PARAMETER") v) = throw UnsupportedOperationException()

    override fun <R, D> accept(visitor: IrElementVisitor<R, D>, data: D): R {
        throw UnsupportedOperationException()
    }
}
