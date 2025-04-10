package `fun`.adaptive.auto.api

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.auto.model.ItemId

/**
 * Base class for listeners that handle events of an auto collection. The class
 * has three functions for each event: a local, a remote and a common.
 *
 * Local is called when the change has been initiated through the instance the
 * listener is added to. Default implementation calls the common version.
 *
 * Remote is called when the change has been received from a peer auto instance.
 * Default implementation calls the common version.
 *
 * Default implementation of the common version just returns.
 */
abstract class AutoCollectionListener<IT : AdatClass> {

    /**
     * Called when the auto collection has a valid value. This may be a value
     * passed as initial value, a value loaded from a persistence provider or
     * a value received from a peer.
     *
     * **This value is not necessarily synchronized with peers.** Use [onSyncEnd]
     * to make sure that the synchronization process has been finished.
     */
    open fun onInit(value: Collection<IT>) = Unit

    /**
     * @see onChange
     */
    open fun onLocalChange(itemId : ItemId, newValue: IT, oldValue : IT?) = onChange(itemId, newValue, oldValue)
    /**
     * @see onChange
     */
    open fun onRemoteChange(itemId : ItemId, newValue: IT, oldValue : IT?) = onChange(itemId, newValue, oldValue)

    /**
     * Called when:
     *
     * * an item has been added to the collection, [oldValue] is `null` in this case
     * * an item already in the collection has been updated, [oldValue] is the original value in this case
     */
    open fun onChange(itemId : ItemId, newValue: IT, oldValue : IT?) = Unit

    /**
     * @see onRemove
     */
    open fun onLocalRemove(itemId : ItemId, removed : IT?) = onRemove(itemId, removed)
    /**
     * @see onRemove
     */
    open fun onRemoteRemove(itemId : ItemId, removed : IT?) = onRemove(itemId, removed)

    /**
     * Called when an item has been from the collection.
     */
    open fun onRemove(itemId : ItemId, removed : IT?) = Unit

    /**
     * Called when synchronization ended and the collection is up-to-date (in respect
     * to the given peer).
     */
    open fun onSyncEnd() = Unit

    /**
     * Called by the `stop` method of the instance.
     */
    open fun onStop() = Unit
}