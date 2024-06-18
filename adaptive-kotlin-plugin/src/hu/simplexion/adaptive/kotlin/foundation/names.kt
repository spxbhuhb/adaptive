/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.foundation

import hu.simplexion.adaptive.kotlin.common.NamesBase
import org.jetbrains.kotlin.name.FqName

object Strings {
    const val RUNTIME_PACKAGE = "hu.simplexion.adaptive.foundation"
    const val INTERNAL_PACKAGE = "hu.simplexion.adaptive.foundation.internal"
    const val BINDING_PACKAGE = "hu.simplexion.adaptive.foundation.binding"
    const val STRUCTURAL_PACKAGE = "hu.simplexion.adaptive.foundation.fragment"
    const val INSTRUCTION_PACKAGE = "hu.simplexion.adaptive.foundation.instruction"
    const val PRODUCER_PACKAGE = "hu.simplexion.adaptive.foundation.producer"

    const val ADAPTIVE = "Adaptive"
    const val ADAPTIVE_ENTRY = "AdaptiveEntry"

    const val ADAPTIVE_ROOT = "AdaptiveRoot"
    const val ADAPTIVE_ANONYMOUS = "AdaptiveAnonymous"
    const val ADAPTIVE_FRAGMENT = "AdaptiveFragment"
    const val ADAPTIVE_ADAPTER = "AdaptiveAdapter"
    const val ADAPTIVE_SEQUENCE = "AdaptiveSequence"
    const val ADAPTIVE_SELECT = "AdaptiveSelect"
    const val ADAPTIVE_LOOP = "AdaptiveLoop"
    const val BOUND_FRAGMENT_FACTORY = "BoundFragmentFactory"
    const val ADAPTIVE_SUPPORT_FUNCTION = "BoundSupportFunction"
    const val ADAPTIVE_STATE_VARIABLE_BINDING = "AdaptiveStateVariableBinding"
    const val ADAPTIVE_TRANSFORM_INTERFACE = "AdaptiveTransformInterface"

    const val ROOT_FRAGMENT = "rootFragment"

    const val GEN_BUILD = "genBuild"
    const val GEN_PATCH_DESCENDANT = "genPatchDescendant"

    const val CREATE = "create"
    const val MOUNT = "mount"
    const val GEN_PATCH_INTERNAL = "genPatchInternal"

    const val HAVE_TO_PATCH = "haveToPatch"
    const val GET_CREATE_CLOSURE_DIRTY_MASK = "getCreateClosureDirtyMask"
    const val GET_THIS_CLOSURE_DIRTY_MASK = "getThisClosureDirtyMask"
    const val SET_STATE_VARIABLE = "setStateVariable"
    const val GET_CREATE_CLOSURE_VARIABLE = "getCreateClosureVariable"
    const val GET_THIS_CLOSURE_VARIABLE = "getThisClosureVariable"
    const val INVALID_INDEX = "invalidIndex"
    const val LOCAL_BINDING = "localBinding"
    const val SET_BINDING = "setBinding"
    const val GET_PRODUCED_VALUE = "getProducedValue"

    const val ADAPTER = "adapter"
    const val PARENT = "parent"
    const val DECLARATION_INDEX = "declarationIndex"
    const val STATE = "state"
    const val SUPPORT_FUNCTION_INDEX = "supportFunctionIndex"
    const val SUPPORT_FUNCTION_RECEIVING_FRAGMENT = "receivingFragment"

    const val HELPER_ADAPTER = "adapter"
    const val HELPER_FRAGMENT = "fragment"
    const val HELPER_THIS_STATE = "thisState"

    const val ADAPTIVE_EXPECT = "AdaptiveExpect"
    const val ADAPTIVE_ACTUAL = "AdaptiveActual"
    const val MANUAL_IMPLEMENTATION = "manualImplementation"

    const val NEW_SEQUENCE = "newSequence"
    const val NEW_SELECT = "newSelect"
    const val NEW_LOOP = "newLoop"

    const val ACTUALIZE = "actualize"

    const val SUPPORT_FUNCTION_INVOKE = "invoke"

    const val KOTLIN_INVOKE = "invoke"

    const val ADD = "add"
    const val FRAGMENT_TYPE = "fragmentType"
    const val NEW_INSTANCE = "newInstance"

    const val INSTRUCTIONS = "instructions" // instruction parameter name

    const val DETACH = "detach"
}

object Names : NamesBase(Strings.RUNTIME_PACKAGE) {
    val PARENT = Strings.PARENT.name()
    val DECLARATION_INDEX = Strings.DECLARATION_INDEX.name()
    val ADAPTER = Strings.ADAPTER.name()
    val HELPER_ADAPTER = Strings.HELPER_ADAPTER.name()
    val KOTLIN_INVOKE = Strings.KOTLIN_INVOKE.name()
    val FRAGMENT_TYPE = Strings.FRAGMENT_TYPE.name()
}

object FqNames {
    fun String.runtime() = FqName(Strings.RUNTIME_PACKAGE + "." + this)
    fun String.structural() = FqName(Strings.STRUCTURAL_PACKAGE + "." + this)

    val ADAPTIVE_ENTRY = Strings.ADAPTIVE_ENTRY.runtime()

    val ADAPTIVE_SEQUENCE = Strings.ADAPTIVE_SEQUENCE.structural()
    val ADAPTIVE_SELECT = Strings.ADAPTIVE_SELECT.structural()
    val ADAPTIVE_LOOP = Strings.ADAPTIVE_LOOP.structural()

    val ADAPTIVE_EXPECT = Strings.ADAPTIVE_EXPECT.runtime()
    val ADAPTIVE_ACTUAL = Strings.ADAPTIVE_ACTUAL.runtime()
}

object ClassIds : NamesBase(Strings.RUNTIME_PACKAGE) {
    private val STRUCTURAL = Strings.STRUCTURAL_PACKAGE.fqName()
    private val BINDING = Strings.BINDING_PACKAGE.fqName()
    private val INTERNAL = Strings.INTERNAL_PACKAGE.fqName()
    private val INSTRUCTION = Strings.INSTRUCTION_PACKAGE.fqName()
    private val PRODUCER_PACKAGE = Strings.PRODUCER_PACKAGE.fqName()

    val ADAPTIVE = Strings.ADAPTIVE.classId()
    val ADAPTIVE_FRAGMENT = Strings.ADAPTIVE_FRAGMENT.classId()
    val ADAPTIVE_ADAPTER = Strings.ADAPTIVE_ADAPTER.classId()

    val ADAPTIVE_ANONYMOUS = Strings.ADAPTIVE_ANONYMOUS.classId { STRUCTURAL }

    val BOUND_FRAGMENT_FACTORY = Strings.BOUND_FRAGMENT_FACTORY.classId { INTERNAL }
    val ADAPTIVE_SUPPORT_FUNCTION = Strings.ADAPTIVE_SUPPORT_FUNCTION.classId { INTERNAL }

    val ADAPTIVE_STATE_VARIABLE_BINDING = Strings.ADAPTIVE_STATE_VARIABLE_BINDING.classId { BINDING }

    val ADAPTIVE_TRANSFORM_INTERFACE = Strings.ADAPTIVE_TRANSFORM_INTERFACE.classId()

    val ADAPTIVE_EXPECT = Strings.ADAPTIVE_EXPECT.classId()

    val ADAPTIVE_INSTRUCTION = "AdaptiveInstruction".classId { INSTRUCTION }
    val ADAPTIVE_DETACH = "AdaptiveDetach".classId { INSTRUCTION }
    val DETACH_HANDLER = "DetachHandler".classId { INSTRUCTION }

    val PRODUCER = "Producer".classId { PRODUCER_PACKAGE }
}

object CallableIds : NamesBase(Strings.RUNTIME_PACKAGE) {
    val HELPER_FUNCTION_ADAPTER = Strings.HELPER_ADAPTER.callableId()
    val HELPER_FUNCTION_FRAGMENT = Strings.HELPER_FRAGMENT.callableId()
    val HELPER_FUNCTION_THIS_STATE = Strings.HELPER_THIS_STATE.callableId()
    val MANUAL_IMPLEMENTATION = Strings.MANUAL_IMPLEMENTATION.callableId()
}

object Indices {

    /**
     * Fragment constructor arguments.
     */
    const val ADAPTIVE_GENERATED_FRAGMENT_ARGUMENT_COUNT = 3

    const val ADAPTIVE_FRAGMENT_ADAPTER = 0
    const val ADAPTIVE_FRAGMENT_PARENT = 1
    const val ADAPTIVE_FRAGMENT_INDEX = 2

    /**
     * `build(parent, declarationIndex)` arguments
     */
    const val BUILD_PARENT = 0
    const val BUILD_DECLARATION_INDEX = 1

    /**
     * `patchDescendant(fragment)` arguments
     */
    const val PATCH_DESCENDANT_FRAGMENT = 0

    /**
     * `setStateVariable(index, value)` arguments
     */
    const val SET_STATE_VARIABLE_ARGUMENT_COUNT = 2

    const val SET_STATE_VARIABLE_INDEX = 0
    const val SET_STATE_VARIABLE_VALUE = 1

    /**
     * `getCreateClosureVariable(index)` arguments
     * `getThisClosureVariable(index)` arguments
     */
    const val GET_CLOSURE_VARIABLE_ARGUMENT_COUNT = 1

    const val GET_CLOSURE_VARIABLE_INDEX = 0

    /**
     * Bridge type parameter for classes.
     */
    const val ADAPTIVE_FRAGMENT_TYPE_INDEX_BRIDGE = 0

    /**
     * BoundFragmentFactory constructor arguments
     */
    const val ADAPTIVE_FRAGMENT_FACTORY_ARGUMENT_COUNT = 2

    const val ADAPTIVE_FRAGMENT_FACTORY_ARGUMENT_DECLARING_FRAGMENT = 0
    const val ADAPTIVE_FRAGMENT_FACTORY_ARGUMENT_DECLARATION_INDEX = 1

    /**
     * Structural fragment state indices
     */
    const val ADAPTIVE_SEQUENCE_ITEM_INDICES = 0
    const val ADAPTIVE_SELECT_BRANCH = 0
    const val ADAPTIVE_LOOP_ITERATOR = 0
    const val ADAPTIVE_LOOP_FACTORY = 1

    /**
     * AdaptiveFragment.invalidIndex(index) arguments
     */
    const val INVALID_INDEX_INDEX = 0
}