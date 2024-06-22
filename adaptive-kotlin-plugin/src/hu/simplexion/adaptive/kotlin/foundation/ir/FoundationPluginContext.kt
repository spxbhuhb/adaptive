/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.foundation.ir

import hu.simplexion.adaptive.kotlin.AdaptiveOptions
import hu.simplexion.adaptive.kotlin.common.AbstractPluginContext
import hu.simplexion.adaptive.kotlin.common.functionByName
import hu.simplexion.adaptive.kotlin.foundation.CallableIds
import hu.simplexion.adaptive.kotlin.foundation.ClassIds
import hu.simplexion.adaptive.kotlin.foundation.Strings
import hu.simplexion.adaptive.kotlin.foundation.ir.arm.ArmClass
import hu.simplexion.adaptive.kotlin.foundation.ir.arm.ArmEntryPoint
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.types.makeNullable
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.functions
import org.jetbrains.kotlin.ir.util.getSimpleFunction
import org.jetbrains.kotlin.ir.util.properties
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName

class FoundationPluginContext(
    irContext: IrPluginContext,
    options: AdaptiveOptions
) : AbstractPluginContext(irContext, options) {

    val irBuiltIns
        get() = irContext.irBuiltIns

    val adaptiveClass = ClassIds.ADAPTIVE.classSymbol()
    val producerAnnotation = ClassIds.PRODUCER.classSymbol()

    val armClasses = mutableListOf<ArmClass>()
    val irClasses = mutableMapOf<FqName, IrClass>()
    val armEntryPoints = mutableListOf<ArmEntryPoint>()

    val adaptiveFragmentClass = ClassIds.ADAPTIVE_FRAGMENT.classSymbol()
    val adaptiveFragmentType = adaptiveFragmentClass.defaultType
    val adaptiveFragmentNType = adaptiveFragmentType.makeNullable()

    val adaptiveAdapterClass = ClassIds.ADAPTIVE_ADAPTER.classSymbol()
    val adaptiveAdapterType = adaptiveAdapterClass.defaultType

    val adaptiveInstructionClass = ClassIds.ADAPTIVE_INSTRUCTION.classSymbol()
    val adaptiveInstructionType = adaptiveInstructionClass.defaultType

    val adaptiveAnonymousClass = ClassIds.ADAPTIVE_ANONYMOUS.classSymbol()
    val anonymousConstructor = adaptiveAnonymousClass.constructors.first { it.owner.valueParameters.size == 4 }

    val boundFragmentFactoryClass = ClassIds.BOUND_FRAGMENT_FACTORY.classSymbol()
    val boundFragmentFactoryType = boundFragmentFactoryClass.defaultType
    val boundFragmentFactoryConstructor = boundFragmentFactoryClass.constructors.single()

    val adaptiveStateVariableBindingClass = ClassIds.ADAPTIVE_STATE_VARIABLE_BINDING.classSymbol()

    val adaptiveTransformInterfaceClass = ClassIds.ADAPTIVE_TRANSFORM_INTERFACE.classSymbol()

    val adapter = Strings.ADAPTER.fragmentPropertyList().single().owner
    val index = Strings.DECLARATION_INDEX.fragmentPropertyList()
    val parent = Strings.PARENT.fragmentPropertyList()

    val create = Strings.CREATE.fragmentFunction()
    val mount = Strings.MOUNT.fragmentFunction()

    val haveToPatch = Strings.HAVE_TO_PATCH.fragmentFunction()
    val getCreateClosureDirtyMask = Strings.GET_CREATE_CLOSURE_DIRTY_MASK.fragmentFunction()
    val getCreateClosureVariable = Strings.GET_CREATE_CLOSURE_VARIABLE.fragmentFunction()
    val getThisClosureVariable = Strings.GET_THIS_CLOSURE_VARIABLE.fragmentFunction()
    val setStateVariable = Strings.SET_STATE_VARIABLE.fragmentFunction { it.owner.valueParameters.size == 2 }
    val localBinding = Strings.LOCAL_BINDING.fragmentFunction()
    val setBinding = Strings.SET_BINDING.fragmentFunction()
    val getProducedValue = Strings.GET_PRODUCED_VALUE.fragmentFunction()

    val arrayGet = checkNotNull(irContext.irBuiltIns.arrayClass.getSimpleFunction("get"))

    val helperFunctions = listOf(
        irContext.referenceFunctions(CallableIds.HELPER_FUNCTION_ADAPTER).single(),
        irContext.referenceFunctions(CallableIds.HELPER_FUNCTION_FRAGMENT).single(),
        irContext.referenceFunctions(CallableIds.HELPER_FUNCTION_THIS_STATE).single()
    )

    val manualImplementation = irContext.referenceFunctions(CallableIds.MANUAL_IMPLEMENTATION).single()

    val adaptiveExpectClass = ClassIds.ADAPTIVE_EXPECT.classSymbol()

    val adapterNewSequenceFun = adaptiveAdapterClass.functionByName { Strings.NEW_SEQUENCE }
    val adapterNewSelectFun = adaptiveAdapterClass.functionByName { Strings.NEW_SELECT }
    val adapterNewLoopFun = adaptiveAdapterClass.functionByName { Strings.NEW_LOOP }
    val adapterActualizeFun = adaptiveAdapterClass.functionByName { Strings.ACTUALIZE }

    val adaptiveDetachClass = ClassIds.ADAPTIVE_DETACH.classSymbol()
    val detachHandlerClass = ClassIds.DETACH_HANDLER.classSymbol()
    val detachFun = detachHandlerClass.owner.functionByName { Strings.DETACH }

    private fun String.fragmentPropertyList() =
        adaptiveFragmentClass.owner.properties.filter { it.name.asString() == this }.map { it.symbol }.toList()

    private fun String.fragmentFunction(filter: (IrSimpleFunctionSymbol) -> Boolean = { true }) =
        adaptiveFragmentClass.functions.single { it.owner.name.asString() == this && filter(it) }

    fun getSymbol(target: FqName) =
        irClasses[target]?.symbol
            ?: irContext.referenceClass(ClassId(target.parent(), target.shortName()))

}

