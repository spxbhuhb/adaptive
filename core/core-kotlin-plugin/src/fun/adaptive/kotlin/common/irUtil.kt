/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.kotlin.common

import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.expressions.IrTypeOperator
import org.jetbrains.kotlin.ir.expressions.IrTypeOperatorCall
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.util.functions
import org.jetbrains.kotlin.ir.util.getPropertyGetter
import org.jetbrains.kotlin.ir.util.isSubclassOf
import org.jetbrains.kotlin.ir.util.properties
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
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

fun IrClass.isSubclassOf(superClassSymbol: IrClassSymbol): Boolean =
    this.isSubclassOf(superClassSymbol.owner)

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

val String.asClassId: ClassId
    get() = ClassId(FqName(this.substringBeforeLast('.')), Name.identifier(this.substringAfterLast('.')))

val FqName.companionClassId
    get() = ClassId(parent(), shortName()).createNestedClassId(KotlinNames.COMPANION_OBJECT)

fun IrStatement.removeCoercionToUnit() =
    if (this is IrTypeOperatorCall && this.operator == IrTypeOperator.IMPLICIT_COERCION_TO_UNIT) {
        this.argument
    } else {
        this
    }