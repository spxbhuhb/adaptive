package `fun`.adaptive.auto.api

import `fun`.adaptive.adat.AdatClass

abstract class AutoCollectionListener<A : AdatClass> {

    /**
     * Called when the auto list is initializes the first time.
     */
    open fun onInit(value: List<A>) = Unit

    /**
     * Called when an item is added or removed (a structural change).
     */
    open fun onChange(newValue: List<A>) = Unit

    /**
     * Called when an item is removed from the list.
     */
    open fun onRemove(item : A) = Unit

    /**
     * Called when an item already in the list changes.
     */
    open fun onChange(newValue: A, oldValue : A?) = Unit

    /**
     * Called when synchronization ended and the list is up-to-date (in respect
     * to the given peer).
     */
    open fun onSyncEnd() = Unit

}