package `fun`.adaptive.auto.internal.producer

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.api.validateForContext
import `fun`.adaptive.auto.api.AutoItemListener
import `fun`.adaptive.auto.api.ItemBase
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.producer.AdaptiveProducer
import kotlinx.coroutines.launch

/**
 * @property  disposeInstance  When true the instance should be disposed by [stop].
 */
class AutoItemProducer<IT : AdatClass>(
    override val binding: AdaptiveStateVariableBinding<IT>,
    val disposeInstance: Boolean
) : AutoItemListener<IT>(), AdaptiveProducer<IT> {

    override var latestValue: IT? = null

    var instance: ItemBase<IT>? = null
        set(v) {
            field = v
            // Avoid double patching. Without this the producer returns with null
            // even if the instance has a value already.
            latestValue = v?.valueOrNull
        }

    val adapter
        get() = binding.targetFragment.adapter

    override fun start() {
        // nothing to do here, callbacks handle the data changes
    }

    override fun stop() {
        if (disposeInstance) {
            instance?.stop()
        } else {
            instance?.removeListener(this)
        }
        instance = null
    }

    override fun onInit(itemId: ItemId, value: IT) {
        if (value === latestValue) return // Avoid double patching
        value.validateForContext() // TODO make sure that we don't validate unnecessarily
        adapter.scope.launch {
            latestValue = value
            setDirty()
        }
    }

    override fun onChange(itemId: ItemId, newValue: IT, oldValue: IT?) {
        newValue.validateForContext() // TODO make sure that we don't validate unnecessarily
        adapter.scope.launch {
            latestValue = newValue
            setDirty() // calls patch
        }
    }

    override fun onRemove(itemId: ItemId, removed: IT?) {
        adapter.scope.launch {
            latestValue = null
            setDirty()
        }
    }

    override fun onStop() {
        adapter.scope.launch {
            latestValue = null
            setDirty()
        }
    }

}