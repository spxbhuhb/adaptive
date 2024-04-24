package hu.simplexion.z2.kotlin.common

import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.util.properties
import org.jetbrains.kotlin.name.Name

fun IrClass.property(name : Name) =
    requireNotNull(properties.firstOrNull { it.name == name })
