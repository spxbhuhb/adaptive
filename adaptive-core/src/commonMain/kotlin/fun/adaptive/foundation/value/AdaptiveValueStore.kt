package `fun`.adaptive.foundation.value

import `fun`.adaptive.general.Observable

class AdaptiveValueStore<VT>(
    value: VT
) : Observable<VT>() {

    override var value: VT = value
        set(v) {
            field = v
            notifyListeners()
        }

}