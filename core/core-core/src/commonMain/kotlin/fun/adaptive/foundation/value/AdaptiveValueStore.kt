package `fun`.adaptive.foundation.value

import `fun`.adaptive.general.AbstractObservable

class AdaptiveValueStore<VT>(
    value: VT
) : AbstractObservable<VT>() {

    override var value: VT = value
        set(v) {
            field = v
            notifyListeners()
        }

}