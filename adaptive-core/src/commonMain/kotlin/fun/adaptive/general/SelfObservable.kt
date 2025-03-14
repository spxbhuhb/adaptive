package `fun`.adaptive.general

/**
 * A class that is an observable itself. Useful for patterns like this:
 *
 * ```kotlin
 * class InputState(
 *     val disabled : Boolean
 * ) : SelfObservable<InputState>() {
 *     var disabled by observable(disabled, ::notify)
 * }
 * ```
 *
 * Note that these classes typically **MUTABLE**.
 */
abstract class SelfObservable<VT> : Observable<VT>() {

    @Suppress("UNCHECKED_CAST")
    override var value: VT
        get() = this as VT
        set(_) {
            throw UnsupportedOperationException()
        }

}