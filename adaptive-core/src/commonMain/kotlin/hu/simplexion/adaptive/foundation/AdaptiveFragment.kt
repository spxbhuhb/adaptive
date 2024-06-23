/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.foundation

import hu.simplexion.adaptive.foundation.binding.AdaptivePropertyMetadata
import hu.simplexion.adaptive.foundation.binding.AdaptiveStateVariableBinding
import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.foundation.internal.AdaptiveClosure
import hu.simplexion.adaptive.foundation.internal.StateVariableMask
import hu.simplexion.adaptive.foundation.internal.cleanStateMask
import hu.simplexion.adaptive.foundation.internal.initStateMask
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

    var flags: Int = 0

    var isMounted: Boolean
        get() = (flags and MOUNTED_MASK) != 0
        set(v) {
            if (v) {
                flags = flags or MOUNTED_MASK
            } else {
                flags = flags and MOUNTED_MASK.inv()
            }
        }

    var tracePatterns: Array<out Regex> = adapter.trace
        set(v) {
            field = v
            trace = v.isNotEmpty()
        }

    var trace: Boolean = tracePatterns.isNotEmpty()

    val state: Array<Any?> = arrayOfNulls<Any?>(stateSize)

    @Suppress("LeakingThis") // closure won't do anything with fragments during init
    open val thisClosure: AdaptiveClosure = AdaptiveClosure(arrayOf(this), stateSize)

    open val createClosure: AdaptiveClosure
        get() = parent?.thisClosure ?: thisClosure

    var dirtyMask: StateVariableMask = initStateMask

    var children = mutableListOf<AdaptiveFragment>()

    var producers: MutableList<AdaptiveProducer<*>>? = null

    var bindings: MutableList<AdaptiveStateVariableBinding<*>>? = null

    val instructions: Array<out AdaptiveInstruction>
        get() =
            if (instructionIndex == - 1) {
                null
            } else {
                @Suppress("UNCHECKED_CAST")
                state[instructionIndex] as? Array<out AdaptiveInstruction>
            }
                ?: emptyArray()

    /**
     * True when this is the initial create call of the fragment.
     */
    val isInit
        get() = (dirtyMask == initStateMask)

    // --------------------------------------------------------------------------
    // Functions generated by the plugin
    // --------------------------------------------------------------------------

    open fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment? {
        pluginGenerated("genBuild")
    }

    open fun genPatchDescendant(fragment: AdaptiveFragment) {
        pluginGenerated("genPatchDescendant")
    }

    /**
     * @return  When true, the children will be also patched, when false they won't.
     *          Generated fragments return with true.
     */
    open fun genPatchInternal(): Boolean {
        pluginGenerated("genPatchInternal")
    }

    // --------------------------------------------------------------------------
    // Actual UI support
    // --------------------------------------------------------------------------

    /**
     * Add a fragment to the actual UI.
     *
     * [direct] is:
     * - null, when the fragment is a structural (loop or select)
     * - true, when there is no structural between [fragment] and `this`
     * - false, when there is a structural between [fragment] and `this`
     */
    open fun addActual(fragment: AdaptiveFragment, direct: Boolean?) {
        parent?.addActual(fragment, direct) ?: adapter.addActualRoot(fragment)
    }

    /**
     * See [addActual].
     */
    open fun removeActual(fragment: AdaptiveFragment, direct: Boolean?) {
        parent?.removeActual(fragment, direct) ?: adapter.removeActualRoot(fragment)
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

        children.forEach { it.mount() }
        isMounted = true

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

    open fun unmount() {
        if (trace) trace("before-Unmount")

        children.forEach { it.unmount() }
        isMounted = false

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
    // Producer support
    // --------------------------------------------------------------------------

    fun addProducer(producer: AdaptiveProducer<*>) {
        if (trace) trace("before-Add-Producer", "producer", producer)

        val producers = producers ?: mutableListOf<AdaptiveProducer<*>>().also { producers = it }

        producers.filter { producer.replaces(it) }.forEach { other: AdaptiveProducer<*> ->
            removeProducer(other)
        }

        producers += producer
        // TODO think about starting producers before the fragment is mounted
        // don't forget CommonSlot and NavSegment where the vakue should be initialized quite early
        producer.start()

        if (trace) trace("after-Add-Producer", "producer", producer)
    }

    fun removeProducer(producer: AdaptiveProducer<*>) {
        if (trace) trace("before-Remove-Producer", "producer", producer)

        requireNotNull(producers).remove(producer)
        producer.stop()

        if (trace) trace("after-Remove-Producer", "producer", producer)
    }

    fun getProducedValue(stateVariableIndex : Int) : Any? {
        producers?.forEach { producer ->
            if (producer.hasValueFor(stateVariableIndex)) return producer.value()
        }
        invalidIndex(stateVariableIndex)
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
            indexInSourceClosure = indexInThis,
            targetFragment = descendant,
            indexInTargetState = indexInTarget,
            path = path,
            metadata = AdaptivePropertyMetadata(boundType)
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
    fun localBinding(indexInState: Int, boundType: String) =
        AdaptiveStateVariableBinding<Int>(
            sourceFragment = this,
            indexInSourceState = indexInState,
            indexInSourceClosure = indexInState,
            targetFragment = this,
            indexInTargetState = indexInState,
            path = null,
            metadata = AdaptivePropertyMetadata(boundType)
        )

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
        if (tracePatterns.none { it.matches(point) }) return
        adapter.trace(this, point, "")
    }

    fun trace(point: String, data: Any?) {
        if (tracePatterns.none { it.matches(point) }) return
        adapter.trace(this, point, data.toString())
    }

    fun trace(point: String, label: String, value: Any?) {
        if (tracePatterns.none { it.matches(point) }) return
        adapter.trace(this, point, "$label: $value")
    }

    @OptIn(ExperimentalStdlibApi::class)
    open fun traceWithState(point: String) {
        if (tracePatterns.none { it.matches(point) }) return
        val thisMask = getThisClosureDirtyMask().toHexString()
        val createMask = getCreateClosureDirtyMask().toHexString()
        adapter.trace(this, point, "createMask: 0x$createMask thisMask: 0x$thisMask state: ${stateToTraceString()}")
    }

    open fun stateToTraceString(): String =
        "[" + this.state.contentToString() + "]"

    private fun Array<Any?>.contentToString() =
        this.joinToString(", ") {
            when (it) {
                is Function<*> -> "Function"
                is Array<*> -> it.contentDeepToString()
                else -> it.toString()
            }
        }

    override fun toString(): String =
        "${this::class.simpleName ?: "<unknown>"} @ $id"

}