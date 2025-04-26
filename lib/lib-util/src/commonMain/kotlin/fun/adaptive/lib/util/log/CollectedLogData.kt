package `fun`.adaptive.lib.util.log

import `fun`.adaptive.general.SelfObservable
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.use

class CollectedLogData: SelfObservable<CollectedLogData>() {

    val lock = getLock()

    val items: List<CollectedLogItem>
        get() = lock.use { mutableItems }

    private val mutableItems = mutableListOf<CollectedLogItem>()

    operator fun plusAssign(item : CollectedLogItem) {
        lock.use { mutableItems += item }
        notifyListeners()
    }

    fun clear() {
        lock.use { mutableItems.clear() }
        notifyListeners()
    }

}