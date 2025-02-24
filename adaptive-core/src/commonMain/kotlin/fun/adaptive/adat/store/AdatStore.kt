package `fun`.adaptive.adat.store

import `fun`.adaptive.general.Observable
import kotlin.reflect.KProperty

abstract class AdatStore<A> : Observable<A>() {

    open fun update(original: A, path: Array<String>, value: Any?) {
        throw UnsupportedOperationException()
    }

    open fun update(original : A, new : A) {
        throw UnsupportedOperationException()
    }

    open fun update(new : A) {
        throw UnsupportedOperationException()
    }

    open fun update(original: A, changes: Array<out Pair<KProperty<*>, *>>): A {
        throw UnsupportedOperationException()
    }

    open fun setProblem(path: Array<String>, value: Boolean) {
        throw UnsupportedOperationException()
    }

}