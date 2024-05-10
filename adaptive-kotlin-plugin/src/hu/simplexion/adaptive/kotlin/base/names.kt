/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.base

import hu.simplexion.adaptive.kotlin.common.NamesBase
import org.jetbrains.kotlin.name.FqName

object Strings {
    const val RUNTIME_PACKAGE = "hu.simplexion.adaptive.base"
    const val BINDING_PACKAGE = "hu.simplexion.adaptive.base.binding"
    const val STRUCTURAL_PACKAGE = "hu.simplexion.adaptive.base.structural"

    const val ADAPTIVE = "Adaptive"
    const val ENTRY_FUNCTION = "adaptive"

    const val ADAPTIVE_ROOT = "AdaptiveRoot"
    const val ADAPTIVE_ANONYMOUS = "AdaptiveAnonymous"
    const val ADAPTIVE_FRAGMENT = "AdaptiveFragment"
    const val ADAPTIVE_CLOSURE = "AdaptiveClosure"
    const val ADAPTIVE_ADAPTER = "AdaptiveAdapter"
    const val ADAPTIVE_SEQUENCE = "AdaptiveSequence"
    const val ADAPTIVE_SELECT = "AdaptiveSelect"
    const val ADAPTIVE_LOOP = "AdaptiveLoop"
    const val ADAPTIVE_FRAGMENT_FACTORY = "AdaptiveFragmentFactory"
    const val ADAPTIVE_SUPPORT_FUNCTION = "AdaptiveSupportFunction"
    const val ADAPTIVE_STATE_VARIABLE_BINDING = "AdaptiveStateVariableBinding"
    const val ADAPTIVE_TRANSFORM_INTERFACE = "AdaptiveTransformInterface"

    const val ROOT_FRAGMENT = "rootFragment"

    const val GEN_BUILD = "genBuild"
    const val GEN_PATCH_DESCENDANT = "genPatchDescendant"
    const val GEN_INVOKE = "genInvoke"
    const val GEN_INVOKE_SUSPEND = "genInvokeSuspend"

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

    const val ADAPTER = "adapter"
    const val PARENT = "parent"
    const val INDEX = "index"
    const val STATE = "state"
    const val SUPPORT_FUNCTION_INDEX = "supportFunctionIndex"
    const val SUPPORT_FUNCTION_RECEIVING_FRAGMENT = "receivingFragment"

    const val HELPER_ADAPTER = "adapter"
    const val HELPER_FRAGMENT = "fragment"
    const val HELPER_THIS_STATE = "thisState"

    const val MANUAL_IMPLEMENTATION = "manualImplementation"
    const val DELEGATED = "Delegated"
    const val BUILD = "build"

    const val SUPPORT_FUNCTION_INVOKE = "invoke"

    const val BT = "BT" // type parameter for fragment, Bridge Type
    const val ROOT_BRIDGE = "rootBridge" // property name of the root bridge in the adapter

    const val KOTLIN_INVOKE = "invoke"
}

object Names : NamesBase(Strings.RUNTIME_PACKAGE) {
    val PARENT = Strings.PARENT.name()
    val INDEX = Strings.INDEX.name()
    val ADAPTER = Strings.ADAPTER.name()
    val HELPER_ADAPTER = Strings.HELPER_ADAPTER.name()
    val BT = Strings.BT.name()
    val KOTLIN_INVOKE = Strings.KOTLIN_INVOKE.name()
}

object FqNames {
    fun String.runtime() = FqName(Strings.RUNTIME_PACKAGE + "." + this)
    fun String.structural() = FqName(Strings.STRUCTURAL_PACKAGE + "." + this)

    val ADAPTIVE_ENTRY_FUNCTION = Strings.ENTRY_FUNCTION.runtime()

    val ADAPTIVE_SEQUENCE = Strings.ADAPTIVE_SEQUENCE.structural()
    val ADAPTIVE_SELECT = Strings.ADAPTIVE_SELECT.structural()
    val ADAPTIVE_LOOP = Strings.ADAPTIVE_LOOP.structural()
    val ADAPTIVE_ANONYMOUS = Strings.ADAPTIVE_ANONYMOUS.structural()
}

object ClassIds : NamesBase(Strings.RUNTIME_PACKAGE) {

    val ADAPTIVE = Strings.ADAPTIVE.classId()
    val ADAPTIVE_CLOSURE = Strings.ADAPTIVE_CLOSURE.classId()
    val ADAPTIVE_FRAGMENT = Strings.ADAPTIVE_FRAGMENT.classId()
    val ADAPTIVE_ADAPTER = Strings.ADAPTIVE_ADAPTER.classId()

    val ADAPTIVE_FRAGMENT_FACTORY = Strings.ADAPTIVE_FRAGMENT_FACTORY.classId()
    val ADAPTIVE_SUPPORT_FUNCTION = Strings.ADAPTIVE_SUPPORT_FUNCTION.classId()
    val ADAPTIVE_STATE_VARIABLE_BINDING = Strings.ADAPTIVE_STATE_VARIABLE_BINDING.classId { Strings.BINDING_PACKAGE.fqName() }
    val ADAPTIVE_TRANSFORM_INTERFACE = Strings.ADAPTIVE_TRANSFORM_INTERFACE.classId()

    val DELEGATED = Strings.DELEGATED.classId()

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
    const val ADAPTIVE_FRAGMENT_ARGUMENT_COUNT = 4
    const val ADAPTIVE_ANONYMOUS_FRAGMENT_ARGUMENT_COUNT = 5

    const val ADAPTIVE_FRAGMENT_ADAPTER = 0
    const val ADAPTIVE_FRAGMENT_PARENT = 1
    const val ADAPTIVE_FRAGMENT_INDEX = 2
    const val ADAPTIVE_FRAGMENT_STATE_SIZE = 3
    const val ADAPTIVE_FRAGMENT_FACTORY = 4

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
     * `invoke(supportFunction: AdaptiveSupportFunction<BT>, arguments: Array<out Any?>): Any?` arguments
     */
    const val INVOKE_SUPPORT_FUNCTION = 0
    const val INVOKE_ARGUMENTS = 1

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
     * AdaptiveFragmentFactory constructor arguments
     */
    const val ADAPTIVE_FRAGMENT_FACTORY_ARGUMENT_COUNT = 2

    const val ADAPTIVE_FRAGMENT_FACTORY_ARGUMENT_DECLARING_FRAGMENT = 0
    const val ADAPTIVE_FRAGMENT_FACTORY_ARGUMENT_DECLARATION_INDEX = 1

    /**
     * AdaptiveSupportFunction constructor arguments
     */
    const val ADAPTIVE_SUPPORT_FUNCTION_ARGUMENT_COUNT = 3

    const val ADAPTIVE_SUPPORT_FUNCTION_DECLARING_FRAGMENT = 0
    const val ADAPTIVE_SUPPORT_FUNCTION_RECEIVING_FRAGMENT = 1
    const val ADAPTIVE_SUPPORT_FUNCTION_INDEX = 2

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