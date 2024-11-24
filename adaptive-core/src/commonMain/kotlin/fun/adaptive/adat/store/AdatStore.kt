package `fun`.adaptive.adat.store

import `fun`.adaptive.adat.AdatClass

abstract class AdatStore<A : AdatClass> {

    open fun update(original: A, path: Array<String>, value: Any?) {
        throw UnsupportedOperationException()
    }

    open fun update(original : A, new : A) {
        throw UnsupportedOperationException()
    }

    open fun update(new : A) {
        throw UnsupportedOperationException()
    }

    open fun setProblem(path: Array<String>, value: Boolean) {
        throw UnsupportedOperationException()
    }

}