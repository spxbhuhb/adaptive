package hu.simplexion.z2.kotlin.common

import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.util.functions
import org.jetbrains.kotlin.ir.util.getPropertyGetter
import org.jetbrains.kotlin.ir.util.properties
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.types.checker.SimpleClassicTypeSystemContext.getClassFqNameUnsafe

fun IrClass.property(name: Name) =
    requireNotNull(properties.firstOrNull { it.name == name })

fun IrClass.propertyGetter(nameFun: () -> String): IrSimpleFunctionSymbol {
    val name = nameFun()
    return requireNotNull(properties.firstOrNull { it.name.identifier == name }?.getter).symbol
}

fun IrClass.functionByName(nameFun: () -> String): IrSimpleFunctionSymbol {
    val name = nameFun()
    return functions.single { it.name.identifier == name }.symbol
}

fun IrClassSymbol.property(nameFun: () -> String): IrProperty {
    val name = nameFun()
    return owner.properties.first { it.name.asString() == name }
}

fun IrClassSymbol.propertyGetter(nameFun: () -> String): IrSimpleFunctionSymbol {
    val name = nameFun()
    return checkNotNull(getPropertyGetter(name)) { "Missing property getter for in ${this.getClassFqNameUnsafe()} $name" }
}

fun IrClassSymbol.functionByName(nameFun: () -> String): IrSimpleFunctionSymbol {
    val name = nameFun()
    return functions.single { it.owner.name.asString() == name }
}
