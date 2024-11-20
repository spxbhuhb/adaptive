package `fun`.adaptive.auto.api

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.auto.model.ItemId

/**
 * Base class for listeners that handle events of an auto item. The class
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
abstract class AutoItemListener<IT : AdatClass> {

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

}