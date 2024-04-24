/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir

import hu.simplexion.z2.kotlin.Z2Options
import hu.simplexion.z2.kotlin.adaptive.CallableIds
import hu.simplexion.z2.kotlin.adaptive.ClassIds
import hu.simplexion.z2.kotlin.adaptive.Strings
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmClass
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmEntryPoint
import hu.simplexion.z2.kotlin.common.AbstractPluginContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.name.FqName

class AdaptivePluginContext(
    irContext: IrPluginContext,
    options: Z2Options
) : AbstractPluginContext(irContext, options) {

    val irBuiltIns
        get() = irContext.irBuiltIns

    val adaptiveNamespaceClass = ClassIds.ADAPTIVE_NAMESPACE.classSymbol()

    val armClasses = mutableListOf<ArmClass>()
    val irClasses = mutableMapOf<FqName, IrClass>()
    val armEntryPoints = mutableListOf<ArmEntryPoint>()

    val adaptiveFragmentClass = ClassIds.ADAPTIVE_FRAGMENT.classSymbol()
    val adaptiveAdapterClass = ClassIds.ADAPTIVE_ADAPTER.classSymbol()
    val adaptiveClosureClass = ClassIds.ADAPTIVE_CLOSURE.classSymbol()

    val adaptiveFragmentFactoryClass = ClassIds.ADAPTIVE_FRAGMENT_FACTORY.classSymbol()
    val adaptiveFragmentFactoryConstructor = adaptiveFragmentFactoryClass.constructors.single()

    val adaptiveSupportFunctionClass = ClassIds.ADAPTIVE_SUPPORT_FUNCTION.classSymbol()
    val adaptiveSupportFunctionInvoke = adaptiveSupportFunctionClass.functionByName { Strings.SUPPORT_FUNCTION_INVOKE }
    val adaptiveSupportFunctionIndex = adaptiveSupportFunctionClass.propertyGetter { Strings.SUPPORT_FUNCTION_INDEX }
    val adaptiveSupportFunctionReceivingFragment = adaptiveSupportFunctionClass.propertyGetter { Strings.SUPPORT_FUNCTION_RECEIVING_FRAGMENT }

    val adaptiveStateVariableBindingClass = ClassIds.ADAPTIVE_STATE_VARIABLE_BINDING.classSymbol()

    val adaptiveTransformInterfaceClass = ClassIds.ADAPTIVE_TRANSFORM_INTERFACE.classSymbol()

    val index = Strings.INDEX.fragmentPropertyList()
    val parent = Strings.PARENT.fragmentPropertyList()

    val create = Strings.CREATE.fragmentFunction()
    val mount = Strings.MOUNT.fragmentFunction()

    val haveToPatch = Strings.HAVE_TO_PATCH.fragmentFunction()
    val getCreateClosureDirtyMask = Strings.GET_CREATE_CLOSURE_DIRTY_MASK.fragmentFunction()
    val getThisClosureDirtyMask = Strings.GET_THIS_CLOSURE_DIRTY_MASK.fragmentFunction()
    val getCreateClosureVariable = Strings.GET_CREATE_CLOSURE_VARIABLE.fragmentFunction()
    val getThisClosureVariable = Strings.GET_THIS_CLOSURE_VARIABLE.fragmentFunction()
    val setStateVariable = Strings.SET_STATE_VARIABLE.fragmentFunction { it.owner.valueParameters.size == 2 }
    val localBinding = Strings.LOCAL_BINDING.fragmentFunction()
    val setBinding = Strings.SET_BINDING.fragmentFunction()

    val arrayGet = checkNotNull(irContext.irBuiltIns.arrayClass.getSimpleFunction("get"))

    val helperFunctions = listOf(
        irContext.referenceFunctions(CallableIds.HELPER_FUNCTION_ADAPTER).single(),
        irContext.referenceFunctions(CallableIds.HELPER_FUNCTION_FRAGMENT).single(),
        irContext.referenceFunctions(CallableIds.HELPER_FUNCTION_THIS_STATE).single()
    )

    val manualImplementation = irContext.referenceFunctions(CallableIds.MANUAL_IMPLEMENTATION).single()

    private fun String.fragmentPropertyList() =
        adaptiveFragmentClass.owner.properties.filter { it.name.asString() == this }.map { it.symbol }.toList()

    private fun String.fragmentFunction(filter: (IrSimpleFunctionSymbol) -> Boolean = { true }) =
        adaptiveFragmentClass.functions.single { it.owner.name.asString() == this && filter(it) }

}

