package `fun`.adaptive.auto.api

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.auto.model.ItemId

/**
 * Base class for listeners that handle events of an auto item. The class
 * has three functions for each event (except stop): a local, a remote and a common.
 *
 * Local is called when the change has been initiated through the instance the
 * listener is added to. Default implementation calls the common version.
 *
 * Remote is called when the change has been received from a peer auto instance.
 * Default implementation calls the common version.
 *
 * Default implementation of the common version just returns.
 *
 * **IMPORTANT** If you override local or remote, the common for that event **WON'T** be called for that specific override.
 */
abstract class AutoItemListener<IT : AdatClass> {

    /**
     * Called when the instance value is initialized with a passed parameter
     * or loaded with a persistence provider.
     */
    open fun onLocalInit(itemId : ItemId, value: IT) = onInit(itemId, value)

    /**
     * Called when
     */
    open fun onRemoteInit(itemId : ItemId, value: IT) = onInit(itemId, value)

    /**
     * Called when:
     *
     * 1. The instance receives a value the first time in its lifetime.
     * 2. The instance value is loaded from a persistence.
     * 3. The listener is added to the instance which already has a value for the item value.
     */
    open fun onInit(itemId : ItemId, value: IT) = Unit

    /**
     * @see onChange
     */
    open fun onLocalChange(itemId : ItemId, newValue: IT, oldValue : IT?) = onChange(itemId, newValue, oldValue)

    /**
     * @see onChange
     */
    open fun onRemoteChange(itemId : ItemId, newValue: IT, oldValue : IT?) = onChange(itemId, newValue, oldValue)

    /**
     * Called when an item has been updated.
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
     * Called when this item was a part of an auto collection, and it has been removed from the collection.
     */
    open fun onRemove(itemId : ItemId, removed : IT?) = Unit

    /**
     * Called by the `stop` method of the instance.
     */
    open fun onStop() = Unit

}