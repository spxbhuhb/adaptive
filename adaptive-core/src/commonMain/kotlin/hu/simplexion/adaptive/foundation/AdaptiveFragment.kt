/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.foundation

import hu.simplexion.adaptive.foundation.binding.AdaptivePropertyMetadata
import hu.simplexion.adaptive.foundation.binding.AdaptiveStateVariableBinding
import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.foundation.internal.*
import hu.simplexion.adaptive.foundation.producer.AdaptiveProducer

abstract class AdaptiveFragment(
    val adapter: AdaptiveAdapter,
    val parent: AdaptiveFragment?,
    val declarationIndex: Int,
    val instructionIndex: Int,
    stateSize: Int
) {
    companion object {
        const val MOUNTED_MASK = 0x01
    }

    val id: Long = adapter.newId()

    var flags : Int = 0

    var mounted : Boolean
        get() = (flags and MOUNTED_MASK) != 0
        set(v) {
            if (v) {
                flags = flags or MOUNTED_MASK
            } else {
                flags = flags and MOUNTED_MASK.inv()
            }
        }

    var trace: Boolean = adapter.trace

    val state: Array<Any?> = arrayOfNulls<Any?>(stateSize)

    @Suppress("LeakingThis") // closure won't do anything with fragments during init
    open val thisClosure: AdaptiveClosure = AdaptiveClosure(arrayOf(this), stateSize)

    open val createClosure: AdaptiveClosure
        get() = parent?.thisClosure ?: thisClosure

    var dirtyMask: StateVariableMask = initStateMask

    var children = mutableListOf<AdaptiveFragment>()

    var producers: MutableList<AdaptiveProducer>? = null

    var bindings: MutableList<AdaptiveStateVariableBinding<*>>? = null

    val instructions : Array<out AdaptiveInstruction>
        get() =
            if (instructionIndex == -1) {
                null
            } else {
                @Suppress("UNCHECKED_CAST")
                state[instructionIndex] as? Array<out AdaptiveInstruction>
            }
                ?: emptyArray()

    // --------------------------------------------------------------------------
    // Functions that support the descendants of this fragment
    // --------------------------------------------------------------------------

    open fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment? {
        pluginGenerated("genBuild")
    }

    open fun genPatchDescendant(fragment: AdaptiveFragment) {
        pluginGenerated("genPatchDescendant")
    }

    open fun invoke(supportFunction: BoundSupportFunction, arguments: Array<out Any?>): Any? {
        if (trace) traceSupport("before-Invoke", supportFunction, arguments)

        val result = genInvoke(supportFunction, arguments)

        if (trace) traceSupport("after-Invoke", supportFunction, result)

        return result
    }

    open fun genInvoke(supportFunction: BoundSupportFunction, arguments: Array<out Any?>): Any? {
        pluginGenerated("genInvoke")
    }

    open suspend fun invokeSuspend(supportFunction: BoundSupportFunction, arguments: Array<out Any?>): Any? {
        if (trace) traceSupport("before-Invoke-Suspend", supportFunction, arguments)

        val result = genInvokeSuspend(supportFunction, arguments)

        if (trace) traceSupport("after-Invoke-Suspend", supportFunction, result)

        return result
    }

    open suspend fun genInvokeSuspend(supportFunction: BoundSupportFunction, arguments: Array<out Any?>): Any? {
        pluginGenerated("genInvokeSuspend")
    }

    open fun addActual(fragment: AdaptiveFragment) {
        if (trace) trace("before-addActual")

        parent?.addActual(fragment) ?: adapter.addActual(fragment)

        if (trace) trace("after-addActual")
    }

    open fun removeActual(fragment: AdaptiveFragment) {
        if (trace) trace("before-removeActual")

        parent?.removeActual(fragment) ?: adapter.removeActual(fragment)

        if (trace) trace("after-removeActual")
    }

    // --------------------------------------------------------------------------
    // Functions that operate on the fragment itself
    // --------------------------------------------------------------------------

    open fun create() {
        if (trace) trace("before-Create")

        patch()

        genBuild(this, 0)?.let { children.add(it) }

        if (trace) trace("after-Create")
    }

    open fun mount() {
        if (trace) trace("before-Mount")

        mounted = true
        parent?.addActual(this) ?: adapter.addActual(this)
        children.forEach { it.mount() }

        if (trace) trace("after-Mount")
    }

    open fun patch() {
        patchExternal()
        patchInternal()
    }

    open fun patchExternal() {
        if (trace) traceWithState("before-Patch-External")

        if (parent != null) { // root components has no parent which can patch them
            createClosure.owner.genPatchDescendant(this)
        }

        if (trace) traceWithState("after-Patch-External")
    }

    open fun patchInternal() {
        if (trace) traceWithState("before-Patch-Internal")

        if (genPatchInternal()) {
            children.forEach { it.patch() }
        }

        dirtyMask = cleanStateMask

        if (trace) traceWithState("after-Patch-Internal")
    }

    /**
     * @return  When true, the children will be also patched, when false they won't.
     *          Generated fragments return with true.
     */
    open fun genPatchInternal(): Boolean {
        pluginGenerated("genPatchInternal")
    }

    open fun unmount() {
        if (trace) trace("before-Unmount")

        children.forEach { it.unmount() }
        parent?.removeActual(this) ?: adapter.removeActual(this)
        mounted = false

        if (trace) trace("after-Unmount")
    }

    open fun dispose() {
        if (trace) trace("before-Dispose")

        children.forEach { it.dispose() }

        // converting to array so we can safely remove
        producers?.toTypedArray()?.forEach { removeProducer(it) }
        bindings?.toTypedArray()?.forEach { removeBinding(it) }

        if (trace) trace("after-Dispose")
    }

    // --------------------------------------------------------------------------
    // State and closure functions
    // --------------------------------------------------------------------------

    fun haveToPatch(closureDirtyMask: StateVariableMask, dependencyMask: StateVariableMask): Boolean =
        (dirtyMask == initStateMask) || (closureDirtyMask and dependencyMask) != cleanStateMask

    fun getThisClosureDirtyMask(): StateVariableMask =
        thisClosure.closureDirtyMask()

    fun getCreateClosureDirtyMask(): StateVariableMask =
        createClosure.closureDirtyMask()

    fun getCreateClosureVariable(variableIndex: Int): Any? =
        createClosure.get(variableIndex)

    fun getThisClosureVariable(variableIndex: Int): Any? =
        thisClosure.get(variableIndex)

    fun setStateVariable(index: Int, value: Any?) {
        setStateVariable(index, value, null)
    }

    fun setStateVariable(index: Int, value: Any?, origin: AdaptiveStateVariableBinding<*>?) {
        if (state[index] == value) return

        state[index] = value

        setDirty(index, origin != null)
    }

    fun setDirty(index: Int, callPatch: Boolean) {
        dirtyMask = dirtyMask or (1 shl index)
        if (callPatch) {
            patchInternal()
        }
    }

    // --------------------------------------------------------------------------
    // Producer management
    // --------------------------------------------------------------------------

    fun addProducer(producer: AdaptiveProducer) {
        if (trace) trace("before-Add-Producer", "producer", producer)

        val producers = producers ?: mutableListOf<AdaptiveProducer>().also { producers = it }

        producers.filter { producer.replaces(it) }.forEach { other ->
            removeProducer(other)
        }

        producers += producer
        producer.start()

        if (trace) trace("after-Add-Producer", "producer", producer)
    }

    fun removeProducer(producer: AdaptiveProducer) {
        if (trace) trace("before-Remove-Producer", "producer", producer)

        requireNotNull(producers).remove(producer)
        producer.stop()

        if (trace) trace("after-Remove-Producer", "producer", producer)
    }

    // --------------------------------------------------------------------------
    // Binding management
    // --------------------------------------------------------------------------

    fun addBinding(binding: AdaptiveStateVariableBinding<*>) {
        if (trace) trace("before-Add-Binding", "binding", binding)

        val bindings = bindings ?: mutableListOf<AdaptiveStateVariableBinding<*>>().also { bindings = it }

        bindings += binding

        if (binding.path != null) {
            binding.propertyProvider.addBinding(binding)
        }

        if (trace) trace("after-Add-Binding", "binding", binding)
    }

    /**
     * Creates and sets a binding between a state variable of this fragment and the [descendant] fragment.
     *
     * When [path] is not null, the binding is between a property of a state variable from this
     * fragment and a state variable of the descendant fragment. In this case the state variable
     * must be implement `AdaptivePropertyProvider`.
     */
    fun setBinding(
        indexInThis: Int,
        descendant: AdaptiveFragment,
        indexInTarget: Int,
        path: Array<String>? = null,
        boundType: String,
    ): AdaptiveStateVariableBinding<*> =

        AdaptiveStateVariableBinding<Int>(
            sourceFragment = this,
            indexInSourceState = indexInThis,
            indexInSourceClosure = indexInTarget,
            targetFragment = descendant,
            indexInTargetState = indexInTarget,
            path = path,
            metadata = AdaptivePropertyMetadata(boundType),
            supportFunctionIndex = - 1
        ).also {
            addBinding(it)
            descendant.setStateVariable(indexInTarget, it)
        }

    fun removeBinding(binding: AdaptiveStateVariableBinding<*>) {
        if (trace) trace("before-Remove-Binding", "binding", binding)

        if (binding.path != null) {
            binding.propertyProvider.removeBinding(binding)
        }

        requireNotNull(bindings).remove(binding)

        if (trace) trace("after-Remove-Binding", "binding", binding)
    }

    /**
     * Creates a binding for producer use.
     */
    fun localBinding(indexInState: Int, supportFunctionIndex: Int, boundType: String) =
        AdaptiveStateVariableBinding<Int>(
            sourceFragment = this,
            indexInSourceState = indexInState,
            indexInSourceClosure = indexInState,
            targetFragment = this,
            indexInTargetState = indexInState,
            path = null,
            metadata = AdaptivePropertyMetadata(boundType),
            supportFunctionIndex = supportFunctionIndex
        )

    // --------------------------------------------------------------------------
    // Fragment search
    // --------------------------------------------------------------------------

    inline fun <reified T> single(): AdaptiveFragment =
        mutableListOf<AdaptiveFragment>().also { r -> filter(r) { it is T } }.single()

    open fun single(filterFun: (it: AdaptiveFragment) -> Boolean): AdaptiveFragment =
        mutableListOf<AdaptiveFragment>().also { filter(it, filterFun) }.single()

    open fun filter(filterFun: (it: AdaptiveFragment) -> Boolean): List<AdaptiveFragment> =
        mutableListOf<AdaptiveFragment>().also { filter(it, filterFun) }

    open fun filter(result: MutableList<AdaptiveFragment>, filterFun: (it: AdaptiveFragment) -> Boolean) {
        if (filterFun(this)) {
            result += this
        }
        children.forEach { it.filter(result, filterFun) }
    }

    // --------------------------------------------------------------------------
    // Utility functions
    // --------------------------------------------------------------------------

    fun pluginGenerated(point: String): Nothing {
        ops(
            "pluginGenerated",
            """
                this code should be replaced by the compiler plugin,
                this is probably a bug in Adaptive,
                please open a bug report on GitHub,
                fragment: $this, point: $point
            """
        )
    }

    fun invalidIndex(index: Int): Nothing {
        ops(
            "invalidIndex",
            """
                theoretically this should never happen,
                this is probably a bug in Adaptive (or you've been naughty),
                please open a bug report on GitHub,
                fragment: $this, index: $index
            """
        )
    }

    fun trace(point: String) {
        adapter.trace(this, point, "")
    }

    fun trace(point: String, label: String, value: Any?) {
        adapter.trace(this, point, "$label: $value")
    }

    @OptIn(ExperimentalStdlibApi::class)
    open fun traceWithState(point: String) {
        val thisMask = getThisClosureDirtyMask().toHexString()
        val createMask = getCreateClosureDirtyMask().toHexString()
        adapter.trace(this, point, "createMask: 0x$createMask thisMask: 0x$thisMask state: ${stateToTraceString()}")
    }

    open fun stateToTraceString(): String =
        this.state.contentToString()

    fun traceSupport(point: String, supportFunction: BoundSupportFunction, arguments: Array<out Any?>) {
        adapter.trace(this, point, "$supportFunction arguments: ${arguments.contentToString()}")
    }

    fun traceSupport(point: String, supportFunction: BoundSupportFunction, result: Any?) {
        adapter.trace(this, point, "index: ${supportFunction.supportFunctionIndex} result: $result")
    }

    override fun toString(): String =
        "${this::class.simpleName ?: "<unknown>"} @ $id"

}